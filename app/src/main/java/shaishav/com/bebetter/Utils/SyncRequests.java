package shaishav.com.bebetter.Utils;

import android.content.Context;
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

/**
 * Created by Shaishav on 05-07-2016.
 */
public class SyncRequests {

    Context context;
    RequestQueue queue;

    public SyncRequests(Context context){
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }

    public void syncLesson(Lesson lesson){
        final Lesson tempLesson = lesson;
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE);
        final String temp_user_email = preferences.getString(Constants.POST_USER_EMAIL,"");
        if(temp_user_email.equals(""))
            return;
        StringRequest request = new StringRequest(Request.Method.POST, Constants.HOST + Constants.LESSON, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resp = new JSONObject(response);
                    String server_id = resp.getString("_id");
                    String local_id = resp.getString("localId");
                    LessonSource lessonSource = new LessonSource(context);
                    lessonSource.open();
                    lessonSource.setServerId(server_id,Integer.parseInt(local_id));
                    lessonSource.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put(MySQLiteHelper.COLUMN_TITLE,tempLesson.getTitle());
                params.put(MySQLiteHelper.COLUMN_LESSON,tempLesson.getLesson());
                params.put(MySQLiteHelper.COLUMN_CATEGORY,tempLesson.getCategory());
                params.put(MySQLiteHelper.COLUMN_CREATED_AT,String.valueOf(tempLesson.getCreated_at()));
                params.put(MySQLiteHelper.COLUMN_IS_PUBLIC,String.valueOf(tempLesson.getIs_public()==1));
                params.put(Constants.LOCAL_ID,String.valueOf(tempLesson.getId()));
                params.put(Constants.POST_USER_EMAIL,temp_user_email);
                return params;
            }
        };

        queue.add(request);
    }

    public void syncLesson(List<Lesson> lessons){
        for(Lesson lesson : lessons){
            syncLesson(lesson);
        }
    }
}
