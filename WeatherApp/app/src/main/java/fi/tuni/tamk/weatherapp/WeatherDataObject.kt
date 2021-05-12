package fi.tuni.tamk.weatherapp

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class WeatherDataObject {
    val coord : Coordinate? = null
    val name: String? = null
    val main: MainWeatherData? = null

    fun getLatitude(): Double? {
        return coord?.lat
    }
    fun getLongitude(): Double? {
        return coord?.lon
    }
    fun getTemperature(): Double? {
        return main?.temp
    }

    override fun toString():String {
        return name + coord.toString()
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Coordinate {
    var lat:Double? = null
    var lon: Double? = null

    override fun toString():String {
        return "lat: " + lat + " lon: " + lon
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class MainWeatherData {
    var temp: Double? = null

}
