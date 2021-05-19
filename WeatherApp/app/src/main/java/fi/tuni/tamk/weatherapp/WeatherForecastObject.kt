package fi.tuni.tamk.weatherapp

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class WeatherForecastObject {
    val list:ArrayList<Forecast3h>? = null

    fun getDtTxt(index:Int):String? {
        return list?.get(index)?.dt_txt
    }

    fun getForecastTemp(index: Int):Double? {
        return list?.get(index)?.main?.temp
    }

    fun getIconCode(index:Int):String? {
        return list?.get(index)?.weather?.get(0)?.icon
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Forecast3h {
    val dt_txt:String? = null
    val main:MainForecast? = null
    val weather: List<ForecastDescription>? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class MainForecast {
    val temp:Double? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ForecastDescription {
    val description:String? = null
    val icon:String? = null
}
