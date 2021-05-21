package fi.tuni.tamk.weatherapp

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class WeatherForecastObject {
    val list:ArrayList<Forecast3h>? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Forecast3h {
    val dt:Long? = null
    val main:MainForecast? = null
    val weather: List<ForecastDescription>? = null
    val rain: Rain? = null
    val wind: Wind? = null
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

@JsonIgnoreProperties(ignoreUnknown = true)
class Rain {
    @JsonProperty("3h")
    val threeHour:Double? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Wind {
    val speed:Double? = null
}
