package shaishav.com.bebetter.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import shaishav.com.bebetter.service.WorkflowService

/**
 * Created by shaishav.gandhi on 3/5/18.
 */
class PhoneUnlockedReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {

    if (intent.action == Intent.ACTION_SCREEN_OFF) {
      val serviceIntent = Intent(context, WorkflowService::class.java)
      serviceIntent.action = WorkflowService.ACTION_OFF
      context.startService(serviceIntent)
    } else if (intent.action == Intent.ACTION_SCREEN_ON) {
      val serviceIntent = Intent(context, WorkflowService::class.java)
      serviceIntent.action = WorkflowService.ACTION_ON
      context.startService(serviceIntent)
    }
  }

}