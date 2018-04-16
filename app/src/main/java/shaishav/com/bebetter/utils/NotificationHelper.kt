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

package shaishav.com.bebetter.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import shaishav.com.bebetter.R
import shaishav.com.bebetter.activities.MainActivity
import shaishav.com.bebetter.controller.HomeController
import shaishav.com.bebetter.controller.SummaryController
import shaishav.com.bebetter.extensions.toFormattedTime
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 1/9/18.
 */
class NotificationHelper @Inject constructor(val context: Context) {

  companion object {
    val channelId = "be_better"
  }

  fun createUsageNotification(usage: Long, goal: Long): Notification {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      registerNotificationChannel()
    }

    val notificationIntent = Intent(context, MainActivity::class.java)

    val pendingIntent = PendingIntent.getActivity(context, 0,
            notificationIntent, 0)

    val mContentView = RemoteViews(context.packageName, R.layout.notification)
    mContentView.setImageViewResource(R.id.notifimage, R.drawable.logo)
    mContentView.setTextViewText(R.id.notiftitle, "BeBetter")
    mContentView.setTextColor(R.id.notiftitle, Color.WHITE)
    mContentView.setTextColor(R.id.notiftext, Color.WHITE)
    if (usage > goal) {
      mContentView.setTextColor(R.id.notiftext, Color.RED)
    }

    mContentView.setTextViewText(R.id.notiftext, "Your mobile usage is " + usage.toFormattedTime() + ". " +
            "Usage goal : " + goal.toFormattedTime() + ".")

    return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notif)
            .setCustomContentView(mContentView)
            .setCustomBigContentView(mContentView)
            .setContentIntent(pendingIntent).build()
  }

  fun createDailySummaryNotification() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      registerNotificationChannel()
    }

    val notificationIntent = Intent(context, MainActivity::class.java)
    notificationIntent.putExtra(Constants.SCREEN_NAME, SummaryController.KEY)
    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

    val pendingIntent = PendingIntent.getActivity(context, 0,
            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notif)
            .setContentTitle(context.getString(R.string.usage_summary))
            .setContentText(context.getString(R.string.yesterday_summary))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent).build()

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(1338, notification)
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  private fun registerNotificationChannel() {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val channelId = channelId
    val channelName = "Usage"
    val importance = NotificationManager.IMPORTANCE_LOW
    val notificationChannel = NotificationChannel(channelId, channelName, importance)
    notificationChannel.setSound(null, null)
    notificationChannel.enableVibration(false)
    notificationManager.createNotificationChannel(notificationChannel)
  }

  fun updateNotification(notification: Notification) {

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(1337, notification)

  }

}