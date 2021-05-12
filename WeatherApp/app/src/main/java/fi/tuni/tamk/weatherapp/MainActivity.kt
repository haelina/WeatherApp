package fi.tuni.tamk.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import java.net.HttpURLConnection
import java.net.URL
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets
import kotlin.concurrent.thread
import com.fasterxml.jackson.databind.ObjectMapper

class MainActivity : AppCompatActivity() {
    lateinit var cityName: TextView
    lateinit var latitude: TextView
    lateinit var longitude: TextView
    lateinit var temperature: TextView
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
        temperature = findViewById(R.id.tempValue)
        searchValue = findViewById(R.id.searchValue)
        weatherData = findViewById(R.id.weatherData)
        //createURL("helsinki")
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
            //
            runOnUiThread() {
                cityName.text = currentWeather.name
                latitude.text = currentWeather.getLatitude().toString()
                longitude.text = currentWeather.getLongitude().toString()
                temperature.text = currentWeather.getTemperature().toString() + " \u2103"
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