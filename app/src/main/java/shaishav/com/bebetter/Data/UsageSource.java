package shaishav.com.bebetter.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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
        contentValues.put(MySQLiteHelper.COLUMN_DATE,date);
        contentValues.put(MySQLiteHelper.COLUMN_USAGE, usage);

        long insertId = database.insert(MySQLiteHelper.TABLE_USAGE, null, contentValues);



        Cursor cursor = database.query(MySQLiteHelper.TABLE_USAGE,null,MySQLiteHelper.COLUMN_ID+"="+insertId,null,null,null
                ,MySQLiteHelper.COLUMN_ID+" desc");

        cursor.moveToFirst();



        Usage post = cursorToPost(cursor);
        cursor.close();

        return post;
    }


    public List<Usage> getAllUsages(){
        List<Usage> posts = new ArrayList<Usage>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_USAGE,
                null, null, null, null, null, MySQLiteHelper.COLUMN_ID+" desc");

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

    public List<Usage> getData(long lower_threshold,long higher_threshold){

        List<Usage> posts = new ArrayList<Usage>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_USAGE,
                null, MySQLiteHelper.COLUMN_DATE + " > "+lower_threshold+" AND "+MySQLiteHelper.COLUMN_DATE+" < "+higher_threshold,
                null, null, null, MySQLiteHelper.COLUMN_ID+" ASC");

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

    public List<Usage> getUsagesForBackup(){
        List<Usage> posts = new ArrayList<Usage>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_USAGE,
                null, MySQLiteHelper.COLUMN_SERVER_ID+" = 'NA'", null, null, null, MySQLiteHelper.COLUMN_DATE+" asc");

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

    public void setServerId(String server_id,int id){
        ContentValues cv = new ContentValues();
        cv.put(MySQLiteHelper.COLUMN_SERVER_ID,server_id);

        database.update(MySQLiteHelper.TABLE_USAGE,cv,MySQLiteHelper.COLUMN_ID+" = "+id,null);
    }


    public boolean isExisting(String server_id){

        Cursor cursor = database.query(MySQLiteHelper.TABLE_USAGE,
                null, MySQLiteHelper.COLUMN_SERVER_ID+" = '"+server_id+"'", null, null, null, MySQLiteHelper.COLUMN_CREATED_AT+" desc");

        cursor.moveToFirst();

        if(cursor.getCount()>0)
            return true;
        else
            return false;

    }


    private Usage cursorToPost(Cursor cursor){
        Usage post = new Usage();
        post.setId(cursor.getLong(0));
        post.setDate(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_DATE)));
        post.setUsage(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_USAGE)));
        post.setServer_id(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SERVER_ID)));

        return post;
    }
}
