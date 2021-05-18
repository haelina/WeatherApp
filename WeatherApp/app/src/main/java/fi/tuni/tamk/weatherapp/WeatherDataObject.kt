package fi.tuni.tamk.weatherapp

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class WeatherDataObject {
    val coord : Coordinate? = null
    val name: String? = null
    val main: MainWeatherData? = null
    val weather: List<WeatherDescription>? = null
    val sys: CountryInfo? = null

    fun getLatitude(): Double? {
        return coord?.lat
    }

    fun getLongitude(): Double? {
        return coord?.lon
    }

    fun getCountrycode(): String? {
        return sys?.country
    }

    fun getDescription(): String? {
        return weather?.get(0)?.description
    }

    fun getIconCode(): String? {
        return weather?.get(0)?.icon
    }

    fun getTemperature(): Double? {
        return main?.temp
    }

    fun getMinTemperature(): Double? {
        return main?.temp_min
    }
    fun getMaxTemperature(): Double? {
        return main?.temp_max
    }

    fun getFeelsLikeTemperature(): Double? {
        return main?.feels_like
    }

    override fun toString():String {
        return name + " " + coord.toString()
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
    var feels_like: Double? = null
    var temp_min:Double? = null
    var temp_max:Double? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class WeatherDescription {
    var description: String? = null
    var icon:String? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class CountryInfo {
    var country: String? = null
}
