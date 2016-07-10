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
        Toast.makeText(this,"Service created",Toast.LENGTH_LONG).show();
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        final BroadcastReceiver mReceiver = new PhoneUnlockedReceiver();
        registerReceiver(mReceiver, filter);

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Be Better")
                .setContentText("Your mobile usage is below your target")
                .setPriority(Notification.PRIORITY_MIN)
                .setContentIntent(pendingIntent).build();

        startForeground(1337, notification);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Service started",Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent,flags,startId);
    }

//    public class LocalBinder extends Binder {
//        BackgroundService getService() {
//            return BackgroundService.this;
//        }
//
//    }

    @Override
    public void onDestroy(){

        Toast.makeText(this,"Service destroyed",Toast.LENGTH_LONG).show();
    }
}
