package shaishav.com.bebetter.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import shaishav.com.bebetter.service.UsageService

/**
 * Created by shaishav.gandhi on 3/24/18.
 */
class AppUpdateReceiver: BroadcastReceiver() {

  override fun onReceive(context: Context, p1: Intent?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      context.startForegroundService(Intent(context, UsageService::class.java))
    } else {
      context.startService(Intent(context, UsageService::class.java))
    }
  }

}