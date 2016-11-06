package shaishav.com.bebetter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import shaishav.com.bebetter.service.BackgroundService;

/**
 * Created by shaishavgandhi05 on 11/5/16.
 */

public class BootupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Started", Toast.LENGTH_SHORT).show();
        context.startService(new Intent(context, BackgroundService.class));
    }
}
