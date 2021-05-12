package fi.tuni.tamk.weatherapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.io.IOUtils
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    lateinit var cityName: TextView
    lateinit var latitude: TextView
    lateinit var longitude: TextView
    lateinit var description: TextView
    lateinit var temperature: TextView
    lateinit var minMaxTemp: TextView
    lateinit var feelsLike: TextView
    lateinit var searchValue: EditText
    lateinit var weatherData: LinearLayout
    lateinit var currentWeather: WeatherDataObject

    // function for creating openweathermap url with desired location
    fun createURL(locationName:String): URL {
        // get api key from config.js
        var key = getApiKey()
        var base = "http://api.openweathermap.org/data/2.5/"
        var args = "weather?q=$locationName&units=metric&appid=$key"
        Log.d("MainActivity", "$base$args")
        return URL(base + args)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cityName = findViewById(R.id.locationName)
        latitude = findViewById(R.id.latValue)
        longitude = findViewById(R.id.lonValue)
        description = findViewById(R.id.description)
        temperature = findViewById(R.id.tempValue)
        minMaxTemp = findViewById(R.id.minMaxTemp)
        feelsLike = findViewById(R.id.feelsLike)
        searchValue = findViewById(R.id.searchValue)
        weatherData = findViewById(R.id.weatherData)
    }

    fun clickedSearch(button: View) {
        Log.d("MainActivity", "Clicked search button")
        getWeather(searchValue.text.toString())
        cityName.text = searchValue.text.toString()
        searchValue.text.clear()
    }

    fun getWeather(searchValue: String) {
        Log.d("MainActivity", "Searching for $searchValue")
        thread() {
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
                cityName.text = currentWeather.name
                latitude.text = "latitude: " + currentWeather.getLatitude().toString()
                longitude.text = "longitude " + currentWeather.getLongitude().toString()
                val modifiedDescription = currentWeather.getDescription()?.substring(0, 1)?.toUpperCase() + currentWeather.getDescription()?.substring(1)
                description.text = modifiedDescription
                temperature.text = currentWeather.getTemperature().toString() + " \u2103"
                minMaxTemp.text = "min: " + currentWeather.getMinTemperature().toString() + " ℃ \t\t\tmax: " + currentWeather.getMaxTemperature().toString() + " ℃"
                feelsLike.text = "Feels like: " + currentWeather.getFeelsLikeTemperature().toString() + " ℃"
                weatherData.visibility = View.VISIBLE
            }
        }
    }

    fun parseWeatherData(data:String): WeatherDataObject {
        Log.d("MainActivity", " data before parsing " + data)
        val mp = ObjectMapper()
        val dataObj:WeatherDataObject = mp.readValue(data, WeatherDataObject::class.java)
        Log.d("MainActivity", dataObj.toString())
        return dataObj
    }
}