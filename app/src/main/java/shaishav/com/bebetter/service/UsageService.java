package shaishav.com.bebetter.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import shaishav.com.bebetter.data.source.PreferenceSource;
import shaishav.com.bebetter.receiver.PhoneUnlockedReceiver;
import shaishav.com.bebetter.utils.NotificationHelper;

/**
 * Created by Shaishav on 26-06-2016.
 */
public class UsageService extends Service {
  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();

    // Register for locks and unlocks
    final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
    filter.addAction(Intent.ACTION_SCREEN_OFF);
    filter.addAction(Intent.ACTION_USER_PRESENT);
    final BroadcastReceiver mReceiver = new PhoneUnlockedReceiver();
    registerReceiver(mReceiver, filter);

    PreferenceSource preferenceSource = PreferenceSource.getInstance(getApplicationContext());

    long usage = preferenceSource.getSessionTime() / (preferenceSource.getUsageUnit());
    long goal = preferenceSource.getGoal() / (preferenceSource.getUsageUnit());

    // TODO: Inject this
    NotificationHelper notif = new NotificationHelper(getApplication());
    Notification notification = notif.createNotification(usage, goal);

    startForeground(1337, notification);

  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return super.onStartCommand(intent, flags, startId);
  }


  @Override
  public void onDestroy() {
    stopForeground(true);
  }
}
