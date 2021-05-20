package fi.tuni.tamk.weatherapp

import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import java.text.SimpleDateFormat
import java.util.*

fun parseWeatherData(data:String): WeatherDataObject {
    Log.d("MainActivity", " data before parsing " + data)
    val mp = ObjectMapper()
    val dataObj:WeatherDataObject = mp.readValue(data, WeatherDataObject::class.java)
    Log.d("MainActivity", dataObj.toString())
    return dataObj
}

fun parseForecastData(data:String): WeatherForecastObject {
    Log.d("MainActivity", " data before parsing " + data)
    val mp = ObjectMapper()
    val dataObj:WeatherForecastObject = mp.readValue(data, WeatherForecastObject::class.java)
    Log.d("MainActivity", dataObj.toString())
    return dataObj
}

fun getDateTimeFormatted(date: Date):String {
    var simpleDateFormat = SimpleDateFormat("dd.MM.yyy HH:mm:ss")
    val formatted = simpleDateFormat.format(date)
    return formatted
}

fun getTimeFormatted(date: Date):String {
    var simpleDateFormat = SimpleDateFormat("HH:mm:ss")
    val formatted = simpleDateFormat.format(date)
    return formatted
}
