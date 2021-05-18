package fi.tuni.tamk.weatherapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.io.IOUtils
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.squareup.picasso.Picasso
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    // city name or location name used in getting weather data
    lateinit var searchValue: EditText

    // latitude and longitude used on gettin weather data
    lateinit var latSearch: TextView
    lateinit var lonSearch: TextView
    var lat = "0.0"
    var lon = "0.0"
    lateinit var searchWithCoordinatesButton: Button

    // linear layout showing weather results
    lateinit var weatherData: LinearLayout
    // linear layout shown when result was not found
    lateinit var noResults: LinearLayout

    // actual data fields containing weather details
    lateinit var cityName: TextView
    lateinit var latitude: TextView
    lateinit var longitude: TextView
    lateinit var description: TextView
    lateinit var temperature: TextView
    lateinit var minMaxTemp: TextView
    lateinit var feelsLike: TextView
    lateinit var weatherIcon: ImageView

    // weather data turned into object
    lateinit var currentWeather: WeatherDataObject

    // variables needed for getting current location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var cancellationTokenSource = CancellationTokenSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchValue = findViewById(R.id.searchValue)

        latSearch = findViewById(R.id.latitude)
        lonSearch = findViewById(R.id.longitude)
        searchWithCoordinatesButton = findViewById(R.id.searchWithCoordinates)

        weatherData = findViewById(R.id.weatherData)
        noResults = findViewById(R.id.notFound)

        cityName = findViewById(R.id.locationName)
        latitude = findViewById(R.id.latValue)
        longitude = findViewById(R.id.lonValue)
        description = findViewById(R.id.description)
        temperature = findViewById(R.id.tempValue)
        minMaxTemp = findViewById(R.id.minMaxTemp)
        feelsLike = findViewById(R.id.feelsLike)
        weatherIcon = findViewById(R.id.weatherIcon)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getWeather("tampere")
    }

    override fun onStop() {
        super.onStop()
        // Cancels possible location request
        cancellationTokenSource.cancel()
    }

    fun clickedSearch(button: View) {
        noResults.visibility = View.GONE
        weatherData.visibility = View.GONE
        Log.d("MainActivity", "Clicked search button")
        getWeather(searchValue.text.toString())
        cityName.text = searchValue.text.toString()
        searchValue.text.clear()
    }

    fun getWeather(searchValue: String) {
        Log.d("MainActivity", "Searching for $searchValue")
        thread() {
            try {
                var url: URL = createURL(searchValue)
                val connection = url.openConnection() as HttpURLConnection
                var result = ""
                val inputStream = connection.inputStream

                // 'use' automatically closes the stream in every case
                inputStream.use {
                    result = IOUtils.toString(it, StandardCharsets.UTF_8)
                }
                if(result != "") {
                    currentWeather = parseWeatherData(result)
                }
                Log.d("MainActivity", result)

                // update ui with runOnUiThread because we are not in main thread here
                runOnUiThread() {
                    updateUi()
                }
            } catch (e: FileNotFoundException) {
                runOnUiThread() {
                    noResults.visibility = View.VISIBLE
                }
            }
        }
    }

    fun updateUi() {
        cityName.text = currentWeather.name + ", " + currentWeather.getCountrycode()
        latitude.text = "latitude: " + currentWeather.getLatitude().toString()
        longitude.text = "longitude " + currentWeather.getLongitude().toString()
        val modifiedDescription = currentWeather.getDescription()?.substring(0, 1)?.toUpperCase() + currentWeather.getDescription()?.substring(1)
        description.text = modifiedDescription
        temperature.text = currentWeather.getTemperature().toString() + " \u2103"
        minMaxTemp.text = "min: " + currentWeather.getMinTemperature().toString() + " ℃ \t\t\tmax: " + currentWeather.getMaxTemperature().toString() + " ℃"
        feelsLike.text = "Feels like: " + currentWeather.getFeelsLikeTemperature().toString() + " ℃"
        Picasso.get().load("http://openweathermap.org/img/wn/${currentWeather.getIconCode()}@2x.png").resize(350, 350).into(weatherIcon);
        weatherData.visibility = View.VISIBLE
    }

    fun clickSearchCoordinates(button: View) {
        getWeatherWithCoordinates(lat, lon)
    }

    fun getWeatherWithCoordinates(lat: String, lon: String) {
        Log.d("MainActivity", "Searching for $searchValue")
        thread() {
            try {
                var url: URL = createURLWithLocation(lat, lon)
                val connection = url.openConnection() as HttpURLConnection
                var result = ""
                val inputStream = connection.inputStream

                // 'use' automatically closes the stream in every case
                inputStream.use {
                    result = IOUtils.toString(it, StandardCharsets.UTF_8)
                }
                if(result != "") {
                    currentWeather = parseWeatherData(result)
                }
                Log.d("MainActivity", result)

                // update ui with runOnUiThread because we are not in main thread here
                runOnUiThread() {
                    updateUi()
                }
            } catch (e: FileNotFoundException) {
                runOnUiThread() {
                    noResults.visibility = View.VISIBLE
                }
            }
        }
    }

    fun checkPermissions():Boolean {
        Log.d("MainActivity", "Checking permissions")
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("MainActivity", "Permissions granted and action can be done")
            return true
        }
        return false
    }

    fun requesPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 100) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getCoordinates(button: View) {
        Log.d("MainActivity", "Finding coordinates")
        if(checkPermissions()) {
            if(isLocationEnabled()) {
                val currentLocTask: Task<Location> = fusedLocationProviderClient.getCurrentLocation(
                    PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token)
                currentLocTask.addOnCompleteListener {
                    if (it.isSuccessful && it.result != null) {
                        val location: Location = it.result
                        Log.d("MainActivity", "thread is ${Thread.currentThread().name}")
                        Log.d("MainActivity", "lat:${location.latitude} lon:${location.longitude}")
                        latSearch.text = "Latitude: ${location.latitude}"
                        lonSearch.text = "Longitude: ${location.longitude}"
                        lat = location.latitude.toString()
                        lon = location.longitude.toString()
                        searchWithCoordinatesButton.isEnabled = true
                    }
                }
            } else {
                Toast.makeText(this, "Location is not enabled", Toast.LENGTH_SHORT).show()
            }
        } else {
            requesPermissions()
        }
    }

    fun isLocationEnabled(): Boolean {
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}
