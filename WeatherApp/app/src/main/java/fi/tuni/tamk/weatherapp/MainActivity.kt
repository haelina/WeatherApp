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

class MainActivity : AppCompatActivity() {
    lateinit var cityName: TextView
    lateinit var searchValue: EditText
    lateinit var weatherData: LinearLayout

    // function for creating openweathermap url with desired location
    fun createURL(locationName:String): URL {
        // Insert your own API key here!
        var key = "c2ab16a9d28b8db7c07573179ae1e689"
        var base = "http://api.openweathermap.org/data/2.5/"
        var args = "weather?q=$locationName&units=metric&appid=$key"
        Log.d("MainActivity", "$base$args")
        return URL(base + args)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cityName = findViewById(R.id.locationName)
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
            Log.d("MainActivity", result)
            runOnUiThread() {
                weatherData.visibility = View.VISIBLE
            }

        }
    }
}