package kr.hs.dongpae.tv.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.fragment_weather.view.*
import kotlinx.coroutines.*
import kr.hs.dongpae.tv.JSManager
import kr.hs.dongpae.tv.R
import kr.hs.dongpae.tv.listObject
import kr.hs.dongpae.tv.listTo
import org.json.JSONObject

class WeatherFragment : Fragment() {

    private var weather: JSManager? = null
    private val list = arrayListOf<WeatherData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)
        view.list_weather.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view.list_weather.adapter = WeatherAdapter(context, list)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        assets/weather.js 코드 로드
        val jsCode = resources.assets.open("weather.js").bufferedReader().use { it.readText() }
//        weather.js 실행
        weather = JSManager.importJS(jsCode, "weather")

//        Coroutine으로 날씨 업데이트 60초마다 실행
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                update()
                delay(60 * 1000)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun update() {

        weather?.call("getWeather")?.let {
            val data = JSONObject(it.toString())
            withContext(Dispatchers.Main) {
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
//                data.getJSONArray("hourly").listObject().forEach { item ->
//                    val index = list.indexOfFirst { i -> i.time == item.getInt("time") }
//                    val wData = WeatherData(
//                        item.getInt("time"),
//                        item.getInt("temperature"),
//                        item.getInt("weather")
//                    )
//                    if (index >= 0) {
//                        if (list.find { i -> i == wData } != null) TODO("건너뛰기")
//                        list[index] = wData
//                        list_weather.adapter?.notifyItemChanged(index)
//                    } else {
//                        list.add(
//                            WeatherData(
//                                item.getInt("time"),
//                                item.getInt("temperature"),
//                                item.getInt("weather")
//                            )
//                        )
//                    }
//                }
            }
        }
    }
}