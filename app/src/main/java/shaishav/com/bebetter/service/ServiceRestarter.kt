/*
 *  Copyright (c) 2018 Shaishav Gandhi
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
 *
 */

package shaishav.com.bebetter.service

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Process


class ServiceRestarter(val application: Application): Thread.UncaughtExceptionHandler {

  override fun uncaughtException(p0: Thread?, p1: Throwable?) {
    val intent = Intent(application, UsageService::class.java)
    val pendingIntent = PendingIntent.getService(application, 0, intent, 0)
    val alarmManager = application.baseContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent)
    Process.killProcess(Process.myPid())
    System.exit(1)
  }
}