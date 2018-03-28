package shaishav.com.bebetter.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import shaishav.com.bebetter.service.UsageService
import shaishav.com.bebetter.service.WorkflowService
import shaishav.com.bebetter.utils.BBApplication
import shaishav.com.bebetter.workflow.UsageWorkflow
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 2/23/18.
 */
class BootReceiver : BroadcastReceiver() {


  override fun onReceive(context: Context, intent: Intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      context.startForegroundService(Intent(context, UsageService::class.java))
    } else {
      context.startService(Intent(context, UsageService::class.java))
    }

    val serviceIntent = Intent(context, WorkflowService::class.java)
    serviceIntent.action = WorkflowService.ACTION_ON
    context.startService(serviceIntent)
  }
}