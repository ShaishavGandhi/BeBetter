package shaishav.com.bebetter.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.data.models.Stat
import shaishav.com.bebetter.data.repository.StatsRepository
import shaishav.com.bebetter.receiver.PhoneUnlockedReceiver
import shaishav.com.bebetter.utils.BBApplication
import shaishav.com.bebetter.utils.NotificationHelper
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 3/2/18.
 */
class UsageService : Service() {

  @Inject lateinit var statsRepository: StatsRepository
  @Inject lateinit var notificationHelper: NotificationHelper
  private val disposables = CompositeDisposable()

  override fun onBind(intent: Intent): IBinder? {
    return null
  }

  override fun onCreate() {
    super.onCreate()

    if (application is BBApplication) {
      (application as BBApplication).addServiceComponent().inject(this)
    }

    // Register for locks and unlocks
    val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
    filter.addAction(Intent.ACTION_SCREEN_OFF)
    filter.addAction(Intent.ACTION_USER_PRESENT)
    val mReceiver = PhoneUnlockedReceiver()
    registerReceiver(mReceiver, filter)

    val disposable = statsRepository.getStat()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { stat ->
              val notification = notificationHelper.createNotification(stat.usage, stat.goal)
              notificationHelper.updateNotification(notification)
              disposables.dispose()
            }

    disposables.add(disposable)

    val notification = notificationHelper.createNotification(0, 200)
    startForeground(1337, notification)
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    return START_STICKY
  }

  override fun onDestroy() {
    stopForeground(true)
    disposables.dispose()
    (application as BBApplication).removeServiceComponent()
  }
}