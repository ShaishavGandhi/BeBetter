package shaishav.com.bebetter.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import shaishav.com.bebetter.service.WorkflowService
import shaishav.com.bebetter.utils.BBApplication
import shaishav.com.bebetter.workflow.UsageWorkflow
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 3/11/18.
 */
class ShutdownReceiver: BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    val serviceIntent = Intent(context, WorkflowService::class.java)
    serviceIntent.action = WorkflowService.ACTION_OFF
    context.startService(serviceIntent)
  }
}