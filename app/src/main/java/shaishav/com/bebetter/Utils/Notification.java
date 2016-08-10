package shaishav.com.bebetter.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.Map;

import shaishav.com.bebetter.Activities.AddLesson;
import shaishav.com.bebetter.Activities.MainActivity;
import shaishav.com.bebetter.Data.PreferenceSource;
import shaishav.com.bebetter.R;

/**
 * Created by Shaishav on 11-07-2016.
 */
public class Notification {

    public android.app.Notification createNotification(Context context,String usage, String goal){

        Intent notificationIntent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        RemoteViews mContentView = new RemoteViews(context.getPackageName(), R.layout.notification);
        mContentView.setImageViewResource(R.id.notifimage, R.drawable.logo);
        mContentView.setTextViewText(R.id.notiftitle, "BeBetter");
        mContentView.setTextColor(R.id.notiftitle,Color.WHITE);
        mContentView.setTextColor(R.id.notiftext,Color.WHITE);
        PreferenceSource preferenceSource = PreferenceSource.getInstance(context);
        if(Long.parseLong(usage) > (preferenceSource.getGoal()/preferenceSource.getUsageUnit()))
            mContentView.setTextColor(R.id.notiftext, Color.RED);

        mContentView.setTextViewText(R.id.notiftext, "Your mobile usage is "+usage+" min. " +
                "Usage goal : "+goal+" min.");

        android.app.Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notif)
                .setCustomContentView(mContentView)
                .setPriority(android.app.Notification.PRIORITY_MIN)
                .setCustomBigContentView(mContentView)
                .setContentIntent(pendingIntent).build();

        return notification;

    }

    public void createReminderNotification(Context context,String name){
        //Create intent for the activity where you enter the lesson
        Intent resultIntent = new Intent(context, AddLesson.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Create the notification
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notif)
                .setContentTitle("Hi "+name)
                .setSound(defaultSoundUri)
                .setContentText("What did you learn today?")
                .setAutoCancel(true);

        //Set intent to notification
        builder.setContentIntent(resultPendingIntent);

        //Notify
        notificationManager.notify(1,builder.build());

    }

    public void createQuoteNotification(Context context,Map<String,String> map){

        String photo = map.get("photo");
        String quote = map.get("quote");
        String author = map.get("author");

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notif)
                .setContentTitle("Quote of the Day")
                .setContentText(quote+" \n-"+author)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(quote+"\n-"+author))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    public void updateNotification(Context context,android.app.Notification notification){

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1337,notification);

    }

}
