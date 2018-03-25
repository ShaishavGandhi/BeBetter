package shaishav.com.bebetter.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import shaishav.com.bebetter.service.UsageService
import shaishav.com.bebetter.utils.BBApplication
import shaishav.com.bebetter.workflow.UsageWorkflow
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 2/23/18.
 */
class BootReceiver : BroadcastReceiver() {

  @Inject lateinit var workflow: UsageWorkflow

  override fun onReceive(context: Context, intent: Intent) {
    (context.applicationContext as BBApplication)
            .addServiceComponent()
            .inject(this)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      context.startForegroundService(Intent(context, UsageService::class.java))
    } else {
      context.startService(Intent(context, UsageService::class.java))
    }

    workflow.phoneUnlocked(Date().time)
  }
}