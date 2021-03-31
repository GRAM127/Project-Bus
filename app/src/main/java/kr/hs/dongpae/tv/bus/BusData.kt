package kr.hs.dongpae.tv.bus

import android.util.Log
import androidx.annotation.ColorRes
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.hs.dongpae.tv.HttpUtils
import org.json.JSONObject

data class BusData(val name: String, @ColorRes val color: Int) {

    private var locationListLeft = listOf<LocationData>()
    private var locationListRight = listOf<LocationData>()

//    ------------

    fun setBusLocation(locationList: List<LocationData>, direction: String) {
        if (direction == LEFT) locationListLeft = locationList
        else locationListRight = locationList
    }

    fun getBusLocation(direction: String): List<LocationData> = if (direction == LEFT) locationListLeft else locationListRight

//    ------------

    companion object {
        const val LEFT = "LEFT"      // 운정 방향 (동패고 -> 한울공원)
        const val RIGHT = "RIGHT"     // 일산 방향 (동패고 -> 동패중)
    }

    data class LocationData(
        @SerializedName("name") val name: String,
        @SerializedName("time") val time: String // TODO("String -> Int")
        )
}
