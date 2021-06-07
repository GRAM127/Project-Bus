package kr.hs.dongpae.tv.weather

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.fragment_weather.view.*
import kotlinx.android.synthetic.main.item_weather.view.*
import kotlinx.coroutines.*
import kr.hs.dongpae.tv.JSManager
import kr.hs.dongpae.tv.R
import kr.hs.dongpae.tv.listObject
import kr.hs.dongpae.tv.listTo
import org.json.JSONArray
import org.json.JSONObject

class WeatherFragment : Fragment() {

    private var weather: JSManager? = null
    private val list = arrayListOf<WeatherData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)
//        view.list_weather.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        view.list_weather.adapter = WeatherAdapter(context, list)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        assets/weather.js 코드 로드
        val jsCode = resources.assets.open("weather.js").bufferedReader().use { it.readText() }
//        weather.js 실행
        weather = JSManager.importJS(jsCode, "weather")

//        Coroutine으로 날씨 업데이트 60초마다 실행
//        TODO("AlarmReceiver로 변경할 것?")
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                update()
                delay(60 * 1000)
            }
        }
    }

    private fun getIcon(weather: String) = when (weather) {
        "맑음" -> R.drawable.ic_weather_sun
        "구름조금" -> R.drawable.ic_weather_sunny
        "구름많음" -> R.drawable.ic_weather_cloud
        "흐림" -> R.drawable.ic_weather_cloudy
        "비", "소나기", "가끔비" -> R.drawable.ic_weather_rain
        "눈", "가끔눈" -> R.drawable.ic_weather_snow
        "천둥번개" -> R.drawable.ic_weather_lightning
        else -> R.drawable.ic_weather_sunny
    }

    @SuppressLint("SetTextI18n")
    private suspend fun update() {

        weather?.call("getWeather")?.let {
            val data = JSONObject(it.toString())

            withContext(Dispatchers.Main) {

                Glide.with(this@WeatherFragment.requireContext()).load(when (data.getString("weather")) {
                    "맑음" -> R.drawable.wallpaper_sky
                    "구름조금", "구름많음" -> R.drawable.wallpaper_cloud
                    "흐림" -> R.drawable.wallpaper_cloud_dack
                    "비", "소나기", "가끔비" -> R.drawable.wallpaper_rain
                    "눈", "가끔눈" -> R.drawable.wallpaper_snow
                    "천둥번개" -> R.drawable.wallpaper_lightning
                    else -> R.drawable.ic_weather_sunny
                }).into(image_weather_background)
//                Glide.with(this@WeatherFragment.requireContext()).load(R.drawable.wallpaper_lightning).into(image_weather_background)

                text_weather_temperature.text = data.getInt("temperature").toString() + "도"
                text_weather_pm10.text = data.getInt("pm10").toString()
                view_weather_pm10.backgroundTintList = when (data.getInt("pm10")) {
                    in 0..30 -> activity!!.getColorStateList(R.color.accentedBlue)
                    in 31..80 -> activity!!.getColorStateList(R.color.accentedGreen)
                    in 81..150 -> activity!!.getColorStateList(R.color.accentedOrange)
                    else -> activity!!.getColorStateList(R.color.accentedRed)
                }
                text_weather_pm10.text = data.getInt("pm2").toString()
                view_weather_pm2.backgroundTintList = when (data.getInt("pm2")) {
                    in 0..30 -> activity!!.getColorStateList(R.color.accentedBlue)
                    in 31..80 -> activity!!.getColorStateList(R.color.accentedGreen)
                    in 81..150 -> activity!!.getColorStateList(R.color.accentedOrange)
                    else -> activity!!.getColorStateList(R.color.accentedRed)
                }

                image_weather.setImageResource(getIcon(data.getString("weather")))

//                TODO("이미지")
//                https://under-desk.tistory.com/183setDrawLabels

                val entryList = arrayListOf<Entry>()
                val viewList = listOf(item_weather_1, item_weather_2, item_weather_3, item_weather_4, item_weather_5)
                JSONArray(data.getString("hourly")).listObject().forEachIndexed { index, item ->
                    entryList.add(Entry(index.toFloat(), item.getInt("temperature").toFloat()))

                    viewList[index].image_weather_item_weather.setImageResource(getIcon(item.getString("weather")))
                    viewList[index].text_weather_item_time.text = "${item.getInt("time")}시"
                    viewList[index].text_weather_item_temperature.text = "${item.getInt("temperature")}°"
                }

                val lineData = LineData()
                val lineDataSet = LineDataSet(entryList, "온도")
                lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                lineDataSet.lineWidth = 10f
                lineDataSet.cubicIntensity = 0.2f
                lineDataSet.lineWidth = 2f
                lineDataSet.setDrawCircles(false)
                lineDataSet.setDrawCircleHole(false)

                lineData.addDataSet(lineDataSet)
                lineData.setValueTextColor(Color.parseColor("#FFFFFFFF"))
                lineData.setValueTextSize(0f)
                chart_weather.data = lineData
                chart_weather.description.text = ""
                chart_weather.axisLeft.isEnabled = false
                chart_weather.axisRight.isEnabled = false
                chart_weather.xAxis.isEnabled = false
                chart_weather.legend.isEnabled = false
                chart_weather.setBackgroundColor(Color.parseColor("#00FFFFFF"))
//                chart_weather.setViewPortOffsets(0f, 0f, 0f, 0f)
                chart_weather.invalidate()
            }
        }
    }
}