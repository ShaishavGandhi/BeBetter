package shaishav.com.bebetter.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shaishav.com.bebetter.Data.Lesson;
import shaishav.com.bebetter.Data.LessonSource;
import shaishav.com.bebetter.Data.MySQLiteHelper;
import shaishav.com.bebetter.Data.Usage;
import shaishav.com.bebetter.Data.UsageSource;

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

    public void syncUsage(Usage usage) {
        final Usage tempUsage = usage;
        if (!checkIfSignedIn())
            return;

        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE);
        final String temp_user_email = preferences.getString(Constants.POST_USER_EMAIL,"");

        StringRequest request = new StringRequest(Request.Method.POST, Constants.HOST + Constants.USAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject obj = new JSONObject(response);
                    String server_id = obj.getString("_id");
                    int local_id = obj.getInt("localId");

                    UsageSource usageSource = new UsageSource(context);
                    usageSource.open();
                    usageSource.setServerId(server_id,local_id);
                    usageSource.close();

                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put(MySQLiteHelper.COLUMN_USAGE,String.valueOf(tempUsage.getUsage()));
                params.put(Constants.LOCAL_ID,String.valueOf(tempUsage.getId()));
                params.put(MySQLiteHelper.COLUMN_DATE,String.valueOf(tempUsage.getDate()));
                params.put(Constants.POST_USER_EMAIL,temp_user_email);
                return params;
            }
        };

        queue.add(request);
    }

    public void syncLesson(Lesson lesson){
        final Lesson tempLesson = lesson;
        if(!checkIfSignedIn())
            return;

        final SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE);
        final String temp_user_email = preferences.getString(Constants.USER,"");

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
                    SharedPreferences.Editor editor = preferences.edit();
                    updateLastBackupTime(editor,resp.getLong("createdAt"));
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

    public void syncUsage(List<Usage> usages){
        for(Usage usage : usages){
            syncUsage(usage);
        }
    }

    public void getSyncedUsages(){
        if(!checkIfSignedIn())
            return;
        final SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE);
        final String temp_user_email = preferences.getString(Constants.EMAIL,"");

        String time = String.valueOf(new Date().getTime());

        StringRequest request = new StringRequest(Request.Method.GET, Constants.HOST + Constants.USAGE + "/" + temp_user_email + "/" + time, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray arr = new JSONArray(response);
                    Log.v("A","Got result of length " +arr.length());
                    for(int i=0;i<arr.length();i++) {
                        JSONObject json = arr.getJSONObject(i);

                        UsageSource usageSource = new UsageSource(context);
                        usageSource.open();
                        if(!usageSource.isExisting(json.getString("_id")))
                        {
                            Usage usage = usageSource.createUsage(json.getLong("date"),json.getLong("usage"));
                            usageSource.setServerId(json.getString("_id"),(int)usage.getId());

                        }
                        usageSource.close();
                    }
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
        });

        queue.add(request);

    }

    public void getSyncedLessons(){
        final SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE);
        final String temp_user_email = preferences.getString(Constants.EMAIL,"");
        if(temp_user_email.equals(""))
            return;

        String time = String.valueOf(preferences.getLong(Constants.LAST_BACKED_UP,0));
        StringRequest request = new StringRequest(Request.Method.GET, Constants.HOST + Constants.LESSON + "/" + temp_user_email + "/" + time, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arr = new JSONArray(response);
                    for(int i=0;i<arr.length();i++) {
                        JSONObject json = arr.getJSONObject(i);
                        JSONArray categories = json.getJSONArray("categories");

                        List<String> cat_list = new ArrayList<>();
                        for (int j = 0; j < categories.length(); j++) {
                            cat_list.add(categories.get(j).toString());
                        }
                        String cat_string = Constants.convertListToString(cat_list);

                        LessonSource lessonSource = new LessonSource(context);
                        lessonSource.open();
                        if(!lessonSource.isExisting(json.getString("_id")))
                        {
                            Lesson lesson = lessonSource.createLesson(json.getString("title"), json.getString("lesson"), cat_string,
                                    json.getLong("createdAt"), json.getBoolean("public"));
                            lessonSource.setServerId(json.getString("_id"),(int)lesson.getId());

                            SharedPreferences.Editor editor = preferences.edit();
                            updateLastBackupTime(editor,json.getLong("createdAt"));
                        }
                        lessonSource.close();
                    }
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
        });

        queue.add(request);
    }

    private void updateLastBackupTime(SharedPreferences.Editor editor,long backup_time){
        editor.putLong(Constants.LAST_BACKED_UP,backup_time);
        editor.commit();
    }

    public boolean checkIfSignedIn(){
        final SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE);
        final String temp_user_email = preferences.getString(Constants.EMAIL,"");
        if(temp_user_email.equals(""))
            return false;
        return true;
    }
}
