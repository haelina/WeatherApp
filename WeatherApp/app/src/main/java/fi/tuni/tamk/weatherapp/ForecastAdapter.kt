package fi.tuni.tamk.weatherapp

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
        var nameField : TextView
        var bmiField : TextView

        init {
            forecastIcon = view.findViewById(R.id.forecastIcon)
            nameField = view.findViewById(R.id.nameText)
            bmiField = view.findViewById(R.id.bmiText)
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
        Picasso.get().load("http://openweathermap.org/img/wn/${forecastItem.weather?.get(0)?.icon}@2x.png").resize(150, 150).into(holder.forecastIcon);

        holder.nameField.text = "${forecastItem.dt_txt}"
        holder.bmiField.text = "temp ${forecastItem.main?.temp} ℃"
    }
}