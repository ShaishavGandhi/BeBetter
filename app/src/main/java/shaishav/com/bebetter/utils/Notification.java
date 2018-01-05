package shaishav.com.bebetter.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import shaishav.com.bebetter.activities.MainActivity;
import shaishav.com.bebetter.data.source.PreferenceSource;
import shaishav.com.bebetter.R;

/**
 * Created by Shaishav on 11-07-2016.
 */
public class Notification {

    public android.app.Notification createNotification(Context context, String usage, String goal) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerNotificationChannel(context);
        }

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

        mContentView.setTextViewText(R.id.notiftext, "Your mobile usage is " + usage + " min. " +
                "Usage goal : " + goal + " min.");

        return new NotificationCompat.Builder(context, "be_better")
                .setSmallIcon(R.drawable.notif)
                .setCustomContentView(mContentView)
                .setPriority(android.app.Notification.PRIORITY_MIN)
                .setCustomBigContentView(mContentView)
                .setContentIntent(pendingIntent).build();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void registerNotificationChannel(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "be_better";
        CharSequence channelName = "Usage";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    public void updateNotification(Context context,android.app.Notification notification){

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1337,notification);

    }

}
