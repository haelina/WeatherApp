package fi.tuni.tamk.weatherapp

import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper

fun parseWeatherData(data:String): WeatherDataObject {
    Log.d("MainActivity", " data before parsing " + data)
    val mp = ObjectMapper()
    val dataObj:WeatherDataObject = mp.readValue(data, WeatherDataObject::class.java)
    Log.d("MainActivity", dataObj.toString())
    return dataObj
}
