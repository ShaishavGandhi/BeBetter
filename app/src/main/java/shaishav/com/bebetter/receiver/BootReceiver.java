package shaishav.com.bebetter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import shaishav.com.bebetter.service.UsageService;

/**
 * Created by shaishavgandhi05 on 11/5/16.
 */

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Started", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, UsageService.class));
        } else {
            context.startService(new Intent(context, UsageService.class));
        }
    }
}
