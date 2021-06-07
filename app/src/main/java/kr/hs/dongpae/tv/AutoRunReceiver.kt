package kr.hs.dongpae.tv

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AutoRunReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
//        BOOT_COMPLETED 받으면 MainActivity 실행
        if(intent?.action == "android.intent.action.BOOT_COMPLETED") {

            val intent = Intent(context, AlarmReceiver::class.java)
//            TODO()

            val activityIntent = Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context?.startActivity(activityIntent)
        }
    }

}