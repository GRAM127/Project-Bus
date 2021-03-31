package kr.hs.dongpae.tv

import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class JsonUtil { }


fun JSONArray.list(): List<JSONObject> {
    val list = mutableListOf<JSONObject>()
    for (i in 0 until length()) {
        list.add(JSONObject(get(i).toString()))
    }
    return list
}

inline fun <reified D> JSONArray.listTo(): List<D> {
    val list = mutableListOf<D>()
    for (i in 0 until length()) {
        list.add(Gson().fromJson(get(i).toString(), D::class.java))
    }
    return list
}