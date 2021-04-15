package kr.hs.dongpae.tv.bus

import androidx.annotation.ColorRes
import com.google.gson.annotations.SerializedName

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
        const val LEFT = "LEFT"         // 일산 방향 (동패고 -> 동패중)
        const val RIGHT = "RIGHT"       // 운정 방향 (동패고 -> 한울공원)
    }

    data class LocationData(
        @SerializedName("name") val name: String,
        @SerializedName("time") val time: String
        )
}
