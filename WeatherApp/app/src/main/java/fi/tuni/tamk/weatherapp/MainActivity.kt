package fi.tuni.tamk.weatherapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.apache.commons.io.IOUtils
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    // city name or location name used in getting weather data
    lateinit var searchValue: EditText

    // linear layouts showing weather and forecast
    lateinit var weatherData: LinearLayout
    lateinit var forecastData: LinearLayout
    // linear layout shown when result was not found
    lateinit var noResults: LinearLayout

    // navigation buttons
    lateinit var weatherButton: Button
    lateinit var forecastButton: Button

    // actual data fields containing weather details
    lateinit var cityName: TextView
    lateinit var latitude: TextView
    lateinit var longitude: TextView
    lateinit var description: TextView
    lateinit var temperature: TextView
    lateinit var minMaxTemp: TextView
    lateinit var feelsLike: TextView
    lateinit var sunriseTime: TextView
    lateinit var sunsetTime: TextView
    lateinit var weatherIcon: ImageView
    lateinit var dateTime: TextView
    lateinit var rain: TextView
    lateinit var wind: TextView

    // cityname in forecast
    lateinit var forecastName: TextView

    // recyclerview for handling forecast items
    lateinit var recycler_view: RecyclerView

    // weather data and forecast data turned into object
    lateinit var currentWeather: WeatherDataObject
    lateinit var currentForecast: WeatherForecastObject

    // variables needed for getting current location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var cancellationTokenSource = CancellationTokenSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchValue = findViewById(R.id.searchValue)

        weatherData = findViewById(R.id.weatherData)
        forecastData = findViewById(R.id.forecastData)
        noResults = findViewById(R.id.notFound)

        weatherButton = findViewById(R.id.weatherButton)
        forecastButton = findViewById(R.id.forecastButton)

        cityName = findViewById(R.id.locationName)
        latitude = findViewById(R.id.latValue)
        longitude = findViewById(R.id.lonValue)
        description = findViewById(R.id.description)
        temperature = findViewById(R.id.tempValue)
        minMaxTemp = findViewById(R.id.minMaxTemp)
        feelsLike = findViewById(R.id.feelsLike)
        weatherIcon = findViewById(R.id.weatherIcon)
        sunriseTime = findViewById(R.id.sunrise)
        sunsetTime = findViewById(R.id.sunset)
        dateTime = findViewById(R.id.dateTime)
        rain = findViewById(R.id.rain)
        wind = findViewById(R.id.wind)

        forecastName = findViewById(R.id.forecastCity)

        recycler_view = findViewById(R.id.recycler_view)
        recycler_view.layoutManager = LinearLayoutManager(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // get weather and forecast for tampere by default
        getAllData("tampere")
    }

    override fun onStop() {
        super.onStop()
        // Cancels possible location request
        cancellationTokenSource.cancel()
    }

    //
    fun clickedSearch(button: View) {
        Log.d("MainActivity", "Clicked search button")
        getAllData(searchValue.text.toString())
        searchValue.text.clear()
    }

    // get weather and forecast data with city or location name
    fun getAllData(city: String) {
        getWeather(city)
        getForecast(city)
    }

    // Set weather data visible and hide other views
    fun resetViews() {
        noResults.visibility = View.GONE
        weatherData.visibility = View.VISIBLE
        forecastData.visibility = View.GONE
    }

    // Set "no results" view visible and disable navbuttons
    fun showNoResults() {
        noResults.isVisible = true
        weatherData.isVisible = false
        forecastData.isVisible = false
        disableNavButtons()
    }

    // make weather data visible when weather button is clicked
    fun showWeather(button: View) {
        resetViews()
        weatherButton.setTextColor(Color.parseColor("#000000"))
        forecastButton.setTextColor(Color.parseColor("#ffffff"))
    }

    // make forecast data visible when forecast button is clicked
    fun showForecast(button: View) {
        forecastData.isVisible = true
        weatherData.isVisible = false
        weatherButton.setTextColor(Color.parseColor("#ffffff"))
        forecastButton.setTextColor(Color.parseColor("#000000"))
    }

    // get weather values from openweathermap
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
                    showNoResults()
                }
            }
        }
    }

    // set navigation button colors to look like enabled
    fun resetNavButtons() {
        ViewCompat.setBackgroundTintList(weatherButton, ContextCompat.getColorStateList(this, R.color.dark_turquoise))
        weatherButton.setTextColor(Color.parseColor("#000000"))
        ViewCompat.setBackgroundTintList(forecastButton, ContextCompat.getColorStateList(this, R.color.dark_turquoise))
        forecastButton.setTextColor(Color.parseColor("#ffffff"))
    }

    // update weather condition values
    fun updateUi() {
        resetNavButtons()
        weatherButton.isEnabled = true
        forecastButton.isEnabled = true
        cityName.text = currentWeather.name + ", " + currentWeather.getCountrycode()
        forecastName.text = currentWeather.name + ", " + currentWeather.getCountrycode()
        latitude.text = "latitude: " + currentWeather.getLatitude().toString()
        longitude.text = "longitude " + currentWeather.getLongitude().toString()
        description.text = currentWeather.getDescription()?.substring(0, 1)?.toUpperCase() + currentWeather.getDescription()?.substring(1)
        temperature.text = currentWeather.getTemperature().toString() + " \u2103"
        minMaxTemp.text = "min: " + currentWeather.getMinTemperature().toString() + " ℃ \t\t\tmax: " + currentWeather.getMaxTemperature().toString() + " ℃"
        feelsLike.text = "Feels like: " + currentWeather.getFeelsLikeTemperature().toString() + " ℃"
        rain.text = if(currentWeather.getRain() != null) "Rain (in last 1h): " + currentWeather.getRain().toString() + " mm" else "Rain (in last 1h): 0.00 mm"
        wind.text = "Wind speed: " + currentWeather.getWind().toString() + " m/s"
        val current = currentWeather.dt
        val timezone = currentWeather.timezone
        if (current != null && timezone != null) {
            val localCurrent = current +  timezone - 10800
            dateTime.text = "${getDateTimeFormatted(Date(localCurrent * 1000L))}"
        }
        val sunrise = currentWeather.getSunriseTime()?.toLong()
        val sunset = currentWeather.getSunsetTime()?.toLong()
        if(sunrise != null && sunset != null && timezone != null) {
            val sunriseDate = Date((sunrise + timezone - 10800) * 1000L)
            val sunsetDate = Date((sunset + timezone - 10800)* 1000L)
            sunriseTime.text = "${getTimeFormatted(sunriseDate)}"
            sunsetTime.text = "${getTimeFormatted(sunsetDate)}"
        }
        Picasso.get().load("http://openweathermap.org/img/wn/${currentWeather.getIconCode()}@2x.png").resize(350, 350).into(weatherIcon);
        resetViews()
    }

    // set buttons disabled and set button backgound and text colors
    fun disableNavButtons() {
        weatherButton.isEnabled = false
        forecastButton.isEnabled = false
        ViewCompat.setBackgroundTintList(weatherButton, ContextCompat.getColorStateList(this, R.color.grey))
        ViewCompat.setBackgroundTintList(forecastButton, ContextCompat.getColorStateList(this, R.color.grey))
        weatherButton.setTextColor(Color.parseColor("#e0e0e0"))
        forecastButton.setTextColor(Color.parseColor("#e0e0e0"))
    }

    fun getWeatherWithCoordinates(lat: String, lon: String) {
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
                    showNoResults()
                }
            }
        }
    }

    fun getForecast(searchValue: String) {
        Log.d("MainActivity", "Searching forecast with $searchValue")
        thread() {
            try {
                var url: URL = getForecastUrl(searchValue)
                val connection = url.openConnection() as HttpURLConnection
                var result = ""
                val inputStream = connection.inputStream

                // 'use' automatically closes the stream in every case
                inputStream.use {
                    result = IOUtils.toString(it, StandardCharsets.UTF_8)
                }
                if(result != "") {
                    currentForecast = parseForecastData(result)
                }
                Log.d("MainActivity", result)

                // update ui with runOnUiThread because we are not in main thread here
                runOnUiThread() {
                    if(currentForecast.list != null && currentWeather.timezone != null) {
                        recycler_view.adapter = ForecastAdapter(currentForecast.list!!, currentWeather.timezone!!)
                        //weatherButton.isEnabled = true
                        //forecastButton.isEnabled = true
                    }
                }

            } catch (e: FileNotFoundException) {
                runOnUiThread() {
                    disableNavButtons()
                    noResults.isVisible = true
                }
            }
        }
    }

    fun getForecastWithCoordinates(lat: String, lon: String) {
        Log.d("MainActivity", "Searching for $searchValue")
        thread() {
            try {
                var url: URL = getForecastUrlWithCoordinates(lat, lon)
                val connection = url.openConnection() as HttpURLConnection
                var result = ""
                val inputStream = connection.inputStream

                // 'use' automatically closes the stream in every case
                inputStream.use {
                    result = IOUtils.toString(it, StandardCharsets.UTF_8)
                }
                if(result != "") {
                    currentForecast = parseForecastData(result)
                }
                Log.d("MainActivity", result)

                // update ui with runOnUiThread because we are not in main thread here
                runOnUiThread() {
                    if(currentForecast.list != null && currentWeather.timezone != null) {
                        recycler_view.adapter = ForecastAdapter(currentForecast.list!!, currentWeather.timezone!!)
                        weatherButton.isEnabled = true
                        forecastButton.isEnabled = true
                    }
                }
            } catch (e: FileNotFoundException) {
                runOnUiThread() {
                    disableNavButtons()
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
                        //Log.d("MainActivity", "thread is ${Thread.currentThread().name}")
                        Log.d("MainActivity", "lat:${location.latitude} lon:${location.longitude}")
                        //hideLayouts()
                        getWeatherWithCoordinates(location.latitude.toString(), location.longitude.toString())
                        getForecastWithCoordinates(location.latitude.toString(), location.longitude.toString())
                    }
                }
            } else {
                Toast.makeText(this, "Location is not enabled", Toast.LENGTH_SHORT).show()
            }
        } else {
            requesPermissions()
        }
    }

    // check if location service is enabled in phone
    fun isLocationEnabled(): Boolean {
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}
