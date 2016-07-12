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

    NotificationManager notificationManager;
    SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        //Create intent for the activity where you enter the lesson
        Intent resultIntent = new Intent(context, AddLesson.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        //Get first name of user
        preferences = context.getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE);
        String name = preferences.getString(Constants.FULL_NAME,"");

        //Create the notification
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notif)
                .setContentTitle("Hi "+name)
                .setContentText("What did you learn today?")
                .setAutoCancel(true);

        //Set intent to notification
        builder.setContentIntent(resultPendingIntent);

        //Notify
        notificationManager.notify(1,builder.build());

    }

}
