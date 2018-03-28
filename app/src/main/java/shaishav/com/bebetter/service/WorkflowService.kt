package shaishav.com.bebetter.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.data.repository.StatsRepository
import shaishav.com.bebetter.utils.BBApplication
import shaishav.com.bebetter.utils.NotificationHelper
import shaishav.com.bebetter.workflow.UsageWorkflow
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 3/25/18.
 */
class WorkflowService : Service() {

  companion object {
    val ACTION_OFF = "action_off"
    val ACTION_ON = "action_on"
  }

  @Inject lateinit var workflow: UsageWorkflow
  @Inject lateinit var notificationHelper: NotificationHelper
  @Inject lateinit var statsRepository: StatsRepository
  val disposables = CompositeDisposable()

  override fun onCreate() {
    super.onCreate()
    (applicationContext as BBApplication)
            .addServiceComponent()
            .inject(this)
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    if (intent?.action == ACTION_OFF) {
      workflow.phoneLocked(Date().time)
      val disposable = statsRepository.getStat()
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe({ stat ->
                notificationHelper.updateNotification(notificationHelper.createNotification(stat.usage, stat.goal))
                this@WorkflowService.stopSelf()
              }, { error ->
                Timber.e(error)
                this@WorkflowService.stopSelf()
              })
      disposables.add(disposable)
    } else if (intent?.action == ACTION_ON) {
      workflow.phoneUnlocked(Date().time)
    }
    return super.onStartCommand(intent, flags, startId)
  }

  override fun onBind(p0: Intent?): IBinder? {
    return null
  }

  override fun onDestroy() {
    super.onDestroy()
    disposables.dispose()
  }
}