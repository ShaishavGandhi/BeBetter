package shaishav.com.bebetter.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import shaishav.com.bebetter.data.contracts.ExperienceContract;
import shaishav.com.bebetter.data.models.Experience;
import shaishav.com.bebetter.data.MySQLiteHelper;

/**
 * Created by Shaishav on 01-03-2016.
 */
public class ExperienceSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private Context context;



    public ExperienceSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Experience createLesson(String title, String lesson, String category, long created_at, boolean is_public){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExperienceContract.COLUMN_LESSON,lesson);
        contentValues.put(ExperienceContract.COLUMN_TITLE, title);
        contentValues.put(ExperienceContract.COLUMN_CATEGORY, category);
        contentValues.put(ExperienceContract.COLUMN_CREATED_AT, created_at);
        if(is_public)
            contentValues.put(ExperienceContract.COLUMN_IS_PUBLIC,1);
        else
            contentValues.put(ExperienceContract.COLUMN_IS_PUBLIC,0);

        long insertId = database.insert(ExperienceContract.TABLE_LESSON, null, contentValues);

        Cursor cursor = database.query(ExperienceContract.TABLE_LESSON, null,
                ExperienceContract.COLUMN_ID+"="+insertId, null, null, null
                , ExperienceContract.COLUMN_CREATED_AT+" desc");

        cursor.moveToFirst();



        Experience post = cursorToPost(cursor);
        cursor.close();

        return post;
    }

    public void setServerId(String server_id,int id){
        ContentValues cv = new ContentValues();
        cv.put(ExperienceContract.COLUMN_SERVER_ID,server_id);

        database.update(ExperienceContract.TABLE_LESSON,cv,ExperienceContract.COLUMN_ID+" = "+id,null);
    }


    public List<Experience> getAllLessons(){
        List<Experience> posts = new ArrayList<Experience>();

        Cursor cursor = database.query(ExperienceContract.TABLE_LESSON,
                null, null, null, null, null, ExperienceContract.COLUMN_CREATED_AT+" desc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Experience post = cursorToPost(cursor);
            posts.add(post);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return posts;
    }

    public List<Experience> getLessonsForBackup(){
        List<Experience> posts = new ArrayList<Experience>();

        Cursor cursor = database.query(ExperienceContract.TABLE_LESSON,
                null, ExperienceContract.COLUMN_SERVER_ID+" = 'NA'", null, null, null, ExperienceContract.COLUMN_CREATED_AT+" desc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Experience post = cursorToPost(cursor);
            posts.add(post);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return posts;
    }

    public boolean isExisting(String server_id){

        Cursor cursor = database.query(ExperienceContract.TABLE_LESSON,
                null, ExperienceContract.COLUMN_SERVER_ID+" = '"+server_id+"'", null, null, null, ExperienceContract.COLUMN_CREATED_AT+" desc");

        cursor.moveToFirst();

        if(cursor.getCount()>0)
            return true;
        else
            return false;

    }

    public static Experience cursorToPost(Cursor cursor){
        Experience post = new Experience();
        post.setId(cursor.getLong(0));
        post.setLesson(cursor.getString(cursor.getColumnIndex(ExperienceContract.COLUMN_LESSON)));
        post.setCreated_at(cursor.getLong(cursor.getColumnIndex(ExperienceContract.COLUMN_CREATED_AT)));
        post.setTitle(cursor.getString(cursor.getColumnIndex(ExperienceContract.COLUMN_TITLE)));
        post.setCategory(cursor.getString(cursor.getColumnIndex(ExperienceContract.COLUMN_CATEGORY)));
        post.setServer_id(cursor.getString(cursor.getColumnIndex(ExperienceContract.COLUMN_SERVER_ID)));
        post.setIs_public(cursor.getInt(cursor.getColumnIndex(ExperienceContract.COLUMN_IS_PUBLIC)));
        return post;
    }


}
