package kr.hs.dongpae.tv.bus

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_bus.view.*
import kotlinx.coroutines.*
import kr.hs.dongpae.tv.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

/*
Bus Information System
    v.0
        기존 버스 시간표 역할
        새로운 디자인 적용
 */

class BISFragment : Fragment() {

    private var busJs: JSManager? = null

    private val busList = listOf(
            BusData("66", R.color.busGreen),
            BusData("150", R.color.busGreen),

            BusData("081", R.color.busYellow),
            BusData("082", R.color.busYellow), // 동패중 방향만 있음
            BusData("083", R.color.busYellow),
            BusData("086", R.color.busYellow)
    )

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bus, container, false).apply {
            list_f_bus_bus.adapter = BusAdapter(context, busList)
            list_f_bus_bus.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val jsCode = resources.assets.open("bus.js").bufferedReader().use { it.readText() }

        busJs = JSManager.importJS(jsCode, "bus")

        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                updateBusData()
                delay(3000)
            }
        }
    }

    private fun updateBusData() {
        busJs?.call("getStationData", arrayOf(BusData.LEFT))?.let {
            JSONArray(it.toString()).list().forEach { item ->
                busList.find { bus -> bus.name == item.getString("name").toString() }?.setBusLocation(item.getJSONArray("locationList").listTo(), BusData.LEFT)
            }
        }
        busJs?.call("getStationData", arrayOf(BusData.RIGHT))?.let {
//            Log.d("#LOG _ RIGHT", it.toString())
            JSONArray(it.toString()).list().forEach { item ->
                busList.find { bus -> bus.name == item.getString("name").toString() }?.setBusLocation(item.getJSONArray("locationList").listTo(), BusData.RIGHT)
            }
        }
    }
}