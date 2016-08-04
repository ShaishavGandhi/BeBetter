package shaishav.com.bebetter.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shaishav.com.bebetter.Data.Lesson;
import shaishav.com.bebetter.Data.LessonSource;
import shaishav.com.bebetter.Data.MySQLiteHelper;
import shaishav.com.bebetter.Data.Usage;
import shaishav.com.bebetter.Data.UsageSource;
import shaishav.com.bebetter.Service.BackupService;
import shaishav.com.bebetter.Utils.Constants;
import shaishav.com.bebetter.Utils.SyncRequests;

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


        Intent serviceIntent = new Intent(context, BackupService.class);
        context.startService(serviceIntent);

    }
}
