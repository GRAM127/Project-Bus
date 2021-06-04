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

//                TODO("배경 -> 라이브 이미지")
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

//                TODO("이미지")
//                TODO("리스트 -> 그래프")
//                https://under-desk.tistory.com/183setDrawLabels

                val entryList = arrayListOf<Entry>()
                val viewList = listOf(item_weather_1, item_weather_2, item_weather_3, item_weather_4, item_weather_5)
                JSONArray(data.getString("hourly")).listObject().forEachIndexed { index, item ->
                    entryList.add(Entry(index.toFloat(), item.getInt("temperature").toFloat()))

//                    val index = list.indexOfFirst { i -> i.time == item.getInt("time") }
//                    val wData = WeatherData(
//                        item.getInt("time"),
//                        item.getInt("temperature"),
////                        item.getString("weather")
//                    0
//                    )
//                    if (index >= 0) {
////                        if (list.find { i -> i == wData } != null) TODO()
////                        list[index] = wData
////                        list_weather.adapter?.notifyItemChanged(index)
//                    } else {
                        viewList[index].text_weather_item_time.text = "${item.getInt("time")}시"
                        viewList[index].text_weather_item_temperature.text = "${item.getInt("temperature")}°"
//                        list.add(wData)
//                        list_weather.adapter?.notifyItemChanged(index)

//                    }
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