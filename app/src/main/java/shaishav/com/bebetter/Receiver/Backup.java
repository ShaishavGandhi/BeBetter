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
import shaishav.com.bebetter.Utils.Constants;
import shaishav.com.bebetter.Utils.SyncRequests;

/**
 * Created by Shaishav on 04-07-2016.
 */
public class Backup extends BroadcastReceiver {

    LessonSource lessonSource;
    List<Lesson> lessons;

    @Override
    public void onReceive(final Context context, Intent intent) {
        lessonSource = new LessonSource(context);
        lessonSource.open();
        lessons = lessonSource.getLessonsForBackup();
        lessonSource.close();

        SyncRequests syncRequests = new SyncRequests(context);
        syncRequests.syncLesson(lessons);

    }
}
