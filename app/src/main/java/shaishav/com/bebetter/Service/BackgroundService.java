package shaishav.com.bebetter.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import shaishav.com.bebetter.Activities.MainActivity;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.Receiver.PhoneUnlockedReceiver;
import shaishav.com.bebetter.Utils.Constants;

/**
 * Created by Shaishav on 26-06-2016.
 */
public class BackgroundService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        final BroadcastReceiver mReceiver = new PhoneUnlockedReceiver();
        registerReceiver(mReceiver, filter);

        long usage = (getSharedPreferences(Constants.PREFERENCES,MODE_PRIVATE).getLong(Constants.SESSION,0))/(1000*60);
        long goal = (getSharedPreferences(Constants.PREFERENCES,MODE_PRIVATE).getInt(Constants.GOAL,200));

        shaishav.com.bebetter.Utils.Notification notif = new shaishav.com.bebetter.Utils.Notification();
        Notification notification = notif.createNotification(this,String.valueOf(usage),String.valueOf(goal));

        startForeground(1337, notification);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent,flags,startId);
    }


    @Override
    public void onDestroy(){

        stopForeground(true);

    }
}
