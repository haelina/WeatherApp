package fi.tuni.tamk.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var cityName: TextView
    lateinit var searchValue: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cityName = findViewById(R.id.locationName)
        searchValue = findViewById(R.id.searchValue)
    }

    fun clickedSearch(button: View) {
        Log.d("MainActivity", "Clicked search button")
        cityName.text = searchValue.text
        searchValue.text.clear()
    }
}