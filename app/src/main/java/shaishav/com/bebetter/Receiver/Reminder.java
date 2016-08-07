package shaishav.com.bebetter.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

import shaishav.com.bebetter.Activities.AddLesson;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.Utils.Constants;

/**
 * Created by Shaishav on 13-06-2016.
 */
public class Reminder extends BroadcastReceiver {

    SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        shaishav.com.bebetter.Utils.Notification notification = new shaishav.com.bebetter.Utils.Notification();
        preferences = context.getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE);
        notification.createReminderNotification(context,preferences.getString(Constants.FULL_NAME,""));

    }

}
