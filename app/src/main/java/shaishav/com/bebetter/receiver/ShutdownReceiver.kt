package shaishav.com.bebetter.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import shaishav.com.bebetter.utils.BBApplication
import shaishav.com.bebetter.workflow.UsageWorkflow
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 3/11/18.
 */
class ShutdownReceiver: BroadcastReceiver() {

  @Inject lateinit var workFlow: UsageWorkflow

  override fun onReceive(context: Context, intent: Intent) {
    (context.applicationContext as BBApplication).addServiceComponent().inject(this)
    workFlow.phoneLocked(Date().time)
  }
}