package shaishav.com.bebetter.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import shaishav.com.bebetter.Activities.MainActivity;
import shaishav.com.bebetter.R;

/**
 * Created by Shaishav on 11-07-2016.
 */
public class Notification {

    public android.app.Notification createNotification(Context context,String usage){
        Intent notificationIntent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        android.app.Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notif)
                .setContentTitle("Be Better")
                .setContentText("Your mobile usage is "+usage+" min")
                .setPriority(android.app.Notification.PRIORITY_MIN)
                .setContentIntent(pendingIntent).build();

        return notification;
    }

    public void updateNotification(Context context,android.app.Notification notification){
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1337,notification);
    }

}
