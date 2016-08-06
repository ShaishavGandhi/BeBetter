package shaishav.com.bebetter.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.util.Map;

import shaishav.com.bebetter.Activities.MainActivity;
import shaishav.com.bebetter.R;

/**
 * Created by Shaishav on 11-07-2016.
 */
public class Notification {

    public android.app.Notification createNotification(Context context,String usage, String goal){
        Intent notificationIntent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        android.app.Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notif)
                .setContentTitle("Be Better")
                .setContentText("Your mobile usage is "+usage+" min. " +
                        "Usage goal : "+goal+" min.")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Your mobile usage is "+usage+" min. " +
                        "Usage goal : "+goal+" min."))
                .setSound(defaultSoundUri)
                .setPriority(android.app.Notification.PRIORITY_MIN)
                .setContentIntent(pendingIntent).build();

        return notification;
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
