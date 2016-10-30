package shaishav.com.bebetter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import shaishav.com.bebetter.service.BackupService;
import shaishav.com.bebetter.utils.Constants;

/**
 * Created by Shaishav on 04-07-2016.
 */
public class Backup extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {

        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        long count = preferences.getLong("onReceive",0);
        editor.putLong("onReceive",++count);
        editor.commit();


        if(preferences.getBoolean(Constants.PREFERENCE_BACKUP,true)) {
            Intent serviceIntent = new Intent(context, BackupService.class);
            context.startService(serviceIntent);
        }

    }
}
