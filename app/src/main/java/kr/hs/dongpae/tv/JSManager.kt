package kr.hs.dongpae.tv

import android.util.Log
import org.mozilla.javascript.Context
import org.mozilla.javascript.Function
import org.mozilla.javascript.ScriptableObject
import java.util.*


class JSManager(private val scope: ScriptableObject) {

//    Javascript 함수 실행 -> return은 Any 로
    fun call(funcName: String, args: Array<Any> = arrayOf()): Any? {
        val rhino = Context.enter()
        try {
            val func = scope.get(funcName, scope) as Function
            val result = func.call(rhino, scope, scope, args)
            return Context.jsToJava(result, Any::class.java)
        } finally {
            Context.exit()
        }
    }

    companion object {
//        Javascript 실행
        fun importJS(script: String, name: String): JSManager? {
            val rhino = Context.enter()
            rhino.optimizationLevel = -1
            rhino.languageVersion = Context.VERSION_1_8

            try {
                val scope = rhino.initStandardObjects()
                rhino.evaluateString(scope, script, name, 1, null)
                return JSManager(scope)
            } catch (e: Exception) {
                Log.e("#LOG", e.toString())
                e.printStackTrace()
            } finally {
                Context.exit()
            }
            return null
        }
    }
}