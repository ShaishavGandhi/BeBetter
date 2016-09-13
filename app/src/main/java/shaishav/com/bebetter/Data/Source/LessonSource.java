package shaishav.com.bebetter.Data.Source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import shaishav.com.bebetter.Data.Models.Lesson;
import shaishav.com.bebetter.Data.MySQLiteHelper;

/**
 * Created by Shaishav on 01-03-2016.
 */
public class LessonSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private Context context;



    public LessonSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Lesson createLesson(String title, String lesson, String category, long created_at, boolean is_public){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteHelper.COLUMN_LESSON,lesson);
        contentValues.put(MySQLiteHelper.COLUMN_TITLE, title);
        contentValues.put(MySQLiteHelper.COLUMN_CATEGORY, category);
        contentValues.put(MySQLiteHelper.COLUMN_CREATED_AT, created_at);
        if(is_public)
            contentValues.put(MySQLiteHelper.COLUMN_IS_PUBLIC,1);
        else
            contentValues.put(MySQLiteHelper.COLUMN_IS_PUBLIC,0);

        long insertId = database.insert(MySQLiteHelper.TABLE_LESSON, null, contentValues);



        Cursor cursor = database.query(MySQLiteHelper.TABLE_LESSON,null,MySQLiteHelper.COLUMN_ID+"="+insertId,null,null,null
                ,MySQLiteHelper.COLUMN_CREATED_AT+" desc");

        cursor.moveToFirst();



        Lesson post = cursorToPost(cursor);
        cursor.close();

        return post;
    }

    public void setServerId(String server_id,int id){
        ContentValues cv = new ContentValues();
        cv.put(MySQLiteHelper.COLUMN_SERVER_ID,server_id);

        database.update(MySQLiteHelper.TABLE_LESSON,cv,MySQLiteHelper.COLUMN_ID+" = "+id,null);
    }


    public List<Lesson> getAllLessons(){
        List<Lesson> posts = new ArrayList<Lesson>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_LESSON,
                null, null, null, null, null, MySQLiteHelper.COLUMN_CREATED_AT+" desc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Lesson post = cursorToPost(cursor);
            posts.add(post);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return posts;
    }

    public List<Lesson> getLessonsForBackup(){
        List<Lesson> posts = new ArrayList<Lesson>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_LESSON,
                null, MySQLiteHelper.COLUMN_SERVER_ID+" = 'NA'", null, null, null, MySQLiteHelper.COLUMN_CREATED_AT+" desc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Lesson post = cursorToPost(cursor);
            posts.add(post);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return posts;
    }

    public boolean isExisting(String server_id){

        Cursor cursor = database.query(MySQLiteHelper.TABLE_LESSON,
                null, MySQLiteHelper.COLUMN_SERVER_ID+" = '"+server_id+"'", null, null, null, MySQLiteHelper.COLUMN_CREATED_AT+" desc");

        cursor.moveToFirst();

        if(cursor.getCount()>0)
            return true;
        else
            return false;

    }

    private Lesson cursorToPost(Cursor cursor){
        Lesson post = new Lesson();
        post.setId(cursor.getLong(0));
        post.setLesson(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_LESSON)));
        post.setCreated_at(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CREATED_AT)));
        post.setTitle(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_TITLE)));
        post.setCategory(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CATEGORY)));
        post.setServer_id(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SERVER_ID)));
        post.setIs_public(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_IS_PUBLIC)));
        return post;
    }


}
