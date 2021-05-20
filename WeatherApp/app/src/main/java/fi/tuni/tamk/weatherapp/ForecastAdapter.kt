package fi.tuni.tamk.weatherapp

import android.text.TextUtils.substring
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ForecastAdapter(val items: ArrayList<Forecast3h>): RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var forecastIcon: ImageView
        var time : TextView
        var forecastDescription: TextView
        var forecastRain: TextView
        var forecastWind: TextView
        var temperature : TextView

        init {
            forecastIcon = view.findViewById(R.id.forecastIcon)
            time = view.findViewById(R.id.time)
            forecastDescription = view.findViewById(R.id.forecastDescription)
            forecastRain = view.findViewById(R.id.forecastRain)
            forecastWind = view.findViewById(R.id.forecastWind)
            temperature = view.findViewById(R.id.temperature)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.forecastitem, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecastItem = items[position]
        Picasso.get().load("http://openweathermap.org/img/wn/${forecastItem.weather?.get(0)?.icon}@2x.png").resize(180, 180).into(holder.forecastIcon);

        holder.time.text = "${forecastItem.dt_txt}"
        holder.forecastDescription.text = "${forecastItem.weather?.get(0)?.description?.substring(0, 1)?.toUpperCase() + forecastItem.weather?.get(0)?.description?.substring(1)}"
        if(forecastItem.rain?.threeHour == null) holder.forecastRain.text = "Rain: 0.00 mm" else holder.forecastRain.text = "Rain: ${forecastItem.rain.threeHour} mm"

        holder.forecastWind.text = "Wind: ${forecastItem.wind?.speed} m/s"
        holder.temperature.text = "${forecastItem.main?.temp} ℃"
    }
}
