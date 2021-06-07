package kr.hs.dongpae.tv

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.SystemClock
import java.util.*

class AutoRunReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if(intent?.action == "android.intent.action.BOOT_COMPLETED") {

            val alarmReceiverIntent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, AlarmReceiver.RECEIVE_ID, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            if (pendingIntent == null) {
                val calendar: Calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, 14)
                }
                (context?.getSystemService(ALARM_SERVICE) as AlarmManager?)?.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_HALF_DAY,
                    pendingIntent
                )
            }

            // BOOT_COMPLETED 받으면 MainActivity 실행
            val activityIntent = Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context?.startActivity(activityIntent)
        }
    }

}