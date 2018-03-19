package shaishav.com.bebetter.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.content.ContextCompat.startForegroundService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.data.models.Stat
import shaishav.com.bebetter.data.repository.StatsRepository
import shaishav.com.bebetter.service.UsageService
import shaishav.com.bebetter.utils.BBApplication
import shaishav.com.bebetter.utils.NotificationHelper
import shaishav.com.bebetter.workflow.UsageWorkflow
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 3/5/18.
 */
class PhoneUnlockedReceiver : BroadcastReceiver() {

  @Inject lateinit var workflow: UsageWorkflow
  @Inject lateinit var notificationHelper: NotificationHelper
  @Inject lateinit var statsRepository: StatsRepository
  val disposables = CompositeDisposable()

  override fun onReceive(context: Context, intent: Intent) {
    (context.applicationContext as BBApplication)
            .addServiceComponent()
            .inject(this)

    if (intent.action == Intent.ACTION_SCREEN_OFF) {

      workflow.phoneLocked(Date().time)
      val disposable = statsRepository.getStat().subscribeOn(Schedulers.io()).subscribeWith(object : DisposableObserver<Stat>() {
        override fun onNext(stat: Stat) {
          notificationHelper.updateNotification(notificationHelper.createNotification(stat.usage, stat.goal))
        }

        override fun onError(e: Throwable) {}

        override fun onComplete() {}
      })
      disposables.add(disposable)
    } else if (intent.action == Intent.ACTION_SCREEN_ON) {
      workflow.phoneUnlocked(Date().time)
    }
  }

}