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
import org.json.JSONObject

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

        busJs = JSManager.importJS(busJsCode, "bus")

        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                updateBusData()
                delay(3000)
            }
        }
    }

    private fun updateBusData() {
        busJs?.call("getBusData", arrayOf(BusData.LEFT))?.let {
            JSONArray(it.toString()).list().forEach { item ->
                busList.find { bus -> bus.name == item.getString("name").toString() }?.setBusLocation(item.getJSONArray("locationList").listTo(), BusData.LEFT)
            }
        }
        busJs?.call("getBusData", arrayOf(BusData.RIGHT))?.let {
            Log.d("#LOG _ RIGHT", it.toString())
            JSONArray(it.toString()).list().forEach { item ->
                busList.find { bus -> bus.name == item.getString("name").toString() }?.setBusLocation(item.getJSONArray("locationList").listTo(), BusData.RIGHT)
            }
        }
    }

//    ------------

    private val busJsCode = """

const BN1 = "66";     //busId = 5516
const BN2 = "150";    //busId = 12018
const BN3 = "081";    //busId = 12072
const BN4 = "082";    //busId = 12013
const BN5 = "083";    //busId = 12029
const BN6 = "086";    //busId = 2800994
        
getBusData = function(direction) {
    let url = org.jsoup.Jsoup.connect(getUrl(direction)).ignoreContentType(true).get().text()
    let json = JSON.parse(url).message.result.busArrivalList
            
    return "[" + json.map(e => {
            var locationList = []
            if(direction=="LEFT"){
                if (e.locationNo1 != null) {
                    locationList.push(
                        {
                            name: String(getName(e.routeId)[Number(getId(e.routeId).findIndex(v => v[0]=='196933'))-Number(e.locationNo1)]), 	// 첫 번째 버스 위치 -> 정류장 이름으로
                            time: String(Number(e.predictTime1)/60).split(".")[0]	// 첫 번째 버스 시간
                        }
                    )
                }
                if (e.locationNo2 != null) {
                    locationList.push(
                        {
                            name: String(getName(e.routeId)[Number(getId(e.routeId).findIndex(v => v[0]=='196933'))-Number(e.locationNo2)]),     // 두 번째 버스 위치 -> 정류장 이름으로
                            time: String(Number(e.predictTime2)/60).split(".")[0] 	 // 두 번째 버스 시간
                        }
                    )
                }
            }else if(direction=="RIGHT"){ 
                if (e.locationNo1 != null) {
                    locationList.push(
                        {
                            name: String(getName(e.routeId)[Number(getId(e.routeId).findIndex(v => v[0]=='196934'))-Number(e.locationNo1)]),	 // 첫 번째 버스 위치 -> 정류장 이름으로
                            time: String(Number(e.predictTime1)/60).split(".")[0]	// 첫 번째 버스 시간
                        }
                    )
                }
                if (e.locationNo2 != null) {
                    locationList.push(
                        {
                            name: String(getName(e.routeId)[Number(getId(e.routeId).findIndex(v => v[0]=='196934'))-Number(e.locationNo2)]),     // 두 번째 버스 위치 -> 정류장 이름으로
                            time: String(Number(e.predictTime2)/60).split(".")[0] 	 // 두 번째 버스 시간
                        }
                    )
                } 
            }
                    
            return JSON.stringify(
                {
                    name: String( e.routeId.replace("5516",BN1).replace("12018",BN2).replace("12072",BN3).replace("12013",BN4).replace("12029",BN5).replace("2800994",BN6) ),       // 이름
                    locationList: locationList
                }
            )
        }
    ) + "]"
}
        
getUrl = function(direction) {
    if (direction == "LEFT") 
        return "https://map.naver.com/v5/api/bus/arrival?lang=ko&stationId=196933&caller=pc_map&output=json"
    else if (direction == "RIGHT") 
         return "https://map.naver.com/v5/api/bus/arrival?lang=ko&stationId=196934&caller=pc_map&output=json"
    else null
}

getId = function(busID) {
    let url = org.jsoup.Jsoup.connect('https://map.naver.com/v5/api/transit/bus/routes/'+busID+'?lang=ko&caller=naver_map&output=json').ignoreContentType(true).get().text()
    let json = JSON.parse(url).busStops
    let IdList = json.map(e => [e.id])
    return IdList  
}

getName = function(busID) {
    let url = org.jsoup.Jsoup.connect('https://map.naver.com/v5/api/transit/bus/routes/'+busID+'?lang=ko&caller=naver_map&output=json').ignoreContentType(true).get().text()
    let json = JSON.parse(url).busStops
    let NameList = json.map(e => [e.name])
    return NameList
}


    """.trimIndent()

}
