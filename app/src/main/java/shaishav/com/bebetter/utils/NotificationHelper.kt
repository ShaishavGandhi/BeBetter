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
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 1/9/18.
 */
class NotificationHelper @Inject constructor(val context: Context) {

  companion object {
    val channelId = "be_better"
  }

  fun createNotification(usage: Long, goal: Long): Notification {
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

    mContentView.setTextViewText(R.id.notiftext, "Your mobile usage is " + usage + " min. " +
            "Usage goal : " + goal + " min.")

    return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notif)
            .setCustomContentView(mContentView)
            .setCustomBigContentView(mContentView)
            .setContentIntent(pendingIntent).build()

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