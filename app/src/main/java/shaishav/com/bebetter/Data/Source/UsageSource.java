package shaishav.com.bebetter.Data.Source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import shaishav.com.bebetter.Data.MySQLiteHelper;
import shaishav.com.bebetter.Data.contracts.UsageContract;
import shaishav.com.bebetter.Data.models.Usage;
import shaishav.com.bebetter.Data.MySQLiteHelper;
import shaishav.com.bebetter.Data.providers.GoalProvider;
import shaishav.com.bebetter.Data.providers.UsageProvider;

/**
 * Created by Shaishav on 26-06-2016.
 */
public class UsageSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private Context context;



    public UsageSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Usage createUsage(long date, long usage){
        ContentValues contentValues = new ContentValues();
        contentValues.put(UsageContract.COLUMN_DATE,date);
        contentValues.put(UsageContract.COLUMN_USAGE, usage);

        long insertId = database.insert(UsageContract.TABLE_USAGE, null, contentValues);



        Cursor cursor = database.query(UsageContract.TABLE_USAGE,null,UsageContract.COLUMN_ID+"="+insertId,null,null,null
                ,UsageContract.COLUMN_ID+" desc");

        cursor.moveToFirst();



        Usage post = cursorToPost(cursor);
        cursor.close();

        return post;
    }


    public List<Usage> getAllUsages(){
        List<Usage> posts = new ArrayList<Usage>();

        Cursor cursor = database.query(UsageContract.TABLE_USAGE,
                null, null, null, null, null, UsageContract.COLUMN_ID+" desc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Usage post = cursorToPost(cursor);
            posts.add(post);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return posts;
    }

    public static List<Usage> getData(Context context, long lower_threshold,long higher_threshold){

        List<Usage> usages = new ArrayList<Usage>();

        String[] args = new String[]{String.valueOf(lower_threshold), String.valueOf(higher_threshold)};

        Cursor cursor = context.getContentResolver().query(UsageProvider.CONTENT_URI, null, UsageProvider.QUERY_SELECTION_ARGS_USAGE_RANGE,
                args, GoalProvider.QUERY_SORT_ORDER);


        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Usage usage = UsageProvider.cursorToUsage(cursor);
            usages.add(usage);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return usages;

    }

    public List<Usage> getUsagesForBackup(){
        List<Usage> posts = new ArrayList<Usage>();

        Cursor cursor = database.query(UsageContract.TABLE_USAGE,
                null, UsageContract.COLUMN_SERVER_ID+" = 'NA'", null, null, null, UsageContract.COLUMN_DATE+" asc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Usage post = cursorToPost(cursor);
            posts.add(post);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return posts;
    }

    public static int getTotalUsage(Context context){
        Cursor cursor = context.getContentResolver().query(UsageProvider.CONTENT_URI,
                new String[]{"MAX(" + UsageContract.COLUMN_USAGE + ") "}, null, null, null);

        if(cursor.moveToFirst())
        {
            return cursor.getInt(0);
        }

        return 0;
    }

    public void setServerId(String server_id,int id){
        ContentValues cv = new ContentValues();
        cv.put(UsageContract.COLUMN_SERVER_ID,server_id);

        database.update(UsageContract.TABLE_USAGE,cv,UsageContract.COLUMN_ID+" = "+id,null);
    }


    public boolean isExisting(String server_id){

        Cursor cursor = database.query(UsageContract.TABLE_USAGE,
                null, UsageContract.COLUMN_SERVER_ID+" = '"+server_id+"'", null, null, null, UsageContract.COLUMN_DATE+" desc");

        cursor.moveToFirst();

        if(cursor.getCount()>0)
            return true;
        else
            return false;

    }


    private Usage cursorToPost(Cursor cursor){
        Usage post = new Usage();
        post.setId(cursor.getLong(0));
        post.setDate(cursor.getLong(cursor.getColumnIndex(UsageContract.COLUMN_DATE)));
        post.setUsage(cursor.getLong(cursor.getColumnIndex(UsageContract.COLUMN_USAGE)));
        post.setServer_id(cursor.getString(cursor.getColumnIndex(UsageContract.COLUMN_SERVER_ID)));

        return post;
    }
}
