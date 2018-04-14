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
    } else if (intent?.action == ACTION_ON) {
      workflow.phoneUnlocked(Date().time)
    }
    val disposable = statsRepository.getStat()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ stat ->
              notificationHelper.updateNotification(notificationHelper.createUsageNotification(stat.usage, stat.goal))
              this@WorkflowService.stopSelf()
            }, { error ->
              Timber.e(error)
              this@WorkflowService.stopSelf()
            })
    disposables.add(disposable)
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