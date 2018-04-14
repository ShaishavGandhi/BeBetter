/*
 * Copyright (c) 2018 Shaishav Gandhi
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions
 *  and limitations under the License.
 */

package shaishav.com.bebetter.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.data.repository.StatsRepository
import shaishav.com.bebetter.receiver.PhoneUnlockedReceiver
import shaishav.com.bebetter.utils.BBApplication
import shaishav.com.bebetter.utils.NotificationHelper
import javax.inject.Inject

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
              val notification = notificationHelper.createUsageNotification(stat.usage, stat.goal)
              notificationHelper.updateNotification(notification)
              disposables.dispose()
            }

    disposables.add(disposable)

    val notification = notificationHelper.createUsageNotification(0, 200 * 1000 * 60)
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