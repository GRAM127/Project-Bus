package kr.hs.dongpae.tv.weather

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_weather.view.*
import kr.hs.dongpae.tv.R

class WeatherAdapter(private val context: Context?, private val list: List<WeatherData>): RecyclerView.Adapter<WeatherAdapter.WeatherHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WeatherHolder(LayoutInflater.from(context).inflate(R.layout.item_weather, parent, false))
    override fun onBindViewHolder(holder: WeatherHolder, position: Int) = holder.bind(list[position])
    override fun getItemCount() = list.size

    inner class WeatherHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(data: WeatherData) {
            with(itemView) {
                image_weather_item_weather
                text_weather_item_temperature.text = data.temperature.toString() + "도"
                text_weather_item_time.text = data.time.toString() + "시"
            }
        }
    }
}