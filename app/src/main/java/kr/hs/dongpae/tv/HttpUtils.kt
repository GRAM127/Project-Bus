package kr.hs.dongpae.tv

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpUtils {

//    https://ju-hy.tistory.com/65
    fun getHttp(uri: String): String? {
        try {
            val u = URL(uri)
            val http = (u.openConnection() as HttpURLConnection).apply {
                connectTimeout = 5000 // 5초 동안 응답 없으면 종료
                readTimeout = 5000 // 5초 동안 응답 없으면 종료
                requestMethod = "GET"
                doOutput = true
            }
            val string = StringBuilder()
            if (http.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(http.inputStream, "utf-8"))
                var readText: String? = ""
                while (reader.readLine().also { readText = it } != null) {
                    string.append(readText)
                }
                reader.close()
            } else Log.e("#LOG", http.responseMessage)
            http.disconnect()
            return string.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    companion object {

        const val serverBIS = ""
    }
}