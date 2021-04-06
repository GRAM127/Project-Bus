package kr.hs.dongpae.tv

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AutoRunReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == "android.intent.action.BOOT_COMPLETED") {
            val activityIntent = Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context?.startActivity(activityIntent)
        }
    }

}