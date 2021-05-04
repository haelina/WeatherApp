package fi.tuni.tamk.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var cityName: TextView
    lateinit var searchValue: EditText

    fun createURL(locationName:String): URL {
        // Insert your own API key here!
        var key = "yourApiKey"
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
        createURL("helsinki")
    }

    fun clickedSearch(button: View) {
        Log.d("MainActivity", "Clicked search button")
        cityName.text = searchValue.text.toString()
        searchValue.text.clear()
    }
}