package shaishav.com.bebetter.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import shaishav.com.bebetter.Data.Source.PreferenceSource;

/**
 * Created by Shaishav on 13-06-2016.
 */
public class Reminder extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        shaishav.com.bebetter.Utils.Notification notification = new shaishav.com.bebetter.Utils.Notification();
        PreferenceSource preferenceSource = PreferenceSource.getInstance(context);
        notification.createReminderNotification(context,preferenceSource.getName());

    }

}
