package shaishav.com.bebetter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import shaishav.com.bebetter.data.source.PreferenceSource;

/**
 * Created by Shaishav on 13-06-2016.
 */
public class Reminder extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        shaishav.com.bebetter.utils.Notification notification = new shaishav.com.bebetter.utils.Notification();
        PreferenceSource preferenceSource = PreferenceSource.getInstance(context);
        notification.createReminderNotification(context,preferenceSource.getName());

    }

}
