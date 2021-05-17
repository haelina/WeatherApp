package fi.tuni.tamk.weatherapp

import android.util.Log
import java.net.URL

// get api key from config.js
val key = getApiKey()
var base = "http://api.openweathermap.org/data/2.5/"

// create openweathermap url with desired location name
fun createURL(locationName:String): URL {
    var args = "weather?q=$locationName&units=metric&appid=$key"
    Log.d("MainActivity", "$base$args")
    return URL(base + args)
}

fun createURLWithLocation(lat:String, lon:String): URL {
    var args = "weather?lat=$lat&lon=$lon&units=metric&appid=$key"
    Log.d("MainActivity", "$base$args")
    return URL(base + args)
}
