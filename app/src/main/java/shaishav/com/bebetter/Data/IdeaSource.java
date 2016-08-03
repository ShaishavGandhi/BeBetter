package shaishav.com.bebetter.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Shaishav on 02-08-2016.
 */
public class IdeaSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private Context context;



    public IdeaSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Idea createIdea(String title){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteHelper.COLUMN_TITLE,title);
        contentValues.put(MySQLiteHelper.COLUMN_DATE, new Date().getTime());

        long insertId = database.insert(MySQLiteHelper.TABLE_IDEAS, null, contentValues);



        Cursor cursor = database.query(MySQLiteHelper.TABLE_IDEAS,null,MySQLiteHelper.COLUMN_DATE+"="+insertId,null,null,null
                ,MySQLiteHelper.COLUMN_ID+" desc");

        cursor.moveToFirst();



        Idea idea = cursorToPost(cursor);
        cursor.close();

        return idea;
    }

    public List<Idea> getAllUsages(){
        List<Idea> ideas = new ArrayList<Idea>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_IDEAS,
                null, null, null, null, null, MySQLiteHelper.COLUMN_DATE+" desc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Idea idea = cursorToPost(cursor);
            ideas.add(idea);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return ideas;
    }

    public List<Idea> getIdeasForBackup(){
        List<Idea> ideas = new ArrayList<Idea>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_IDEAS,
                null, MySQLiteHelper.COLUMN_SERVER_ID+" = 'NA'", null, null, null, MySQLiteHelper.COLUMN_DATE+" asc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Idea idea = cursorToPost(cursor);
            ideas.add(idea);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return ideas;
    }

    public void setServerId(String server_id,int id){
        ContentValues cv = new ContentValues();
        cv.put(MySQLiteHelper.COLUMN_SERVER_ID,server_id);

        database.update(MySQLiteHelper.TABLE_IDEAS,cv,MySQLiteHelper.COLUMN_ID+" = "+id,null);
    }


    public boolean isExisting(String server_id){

        Cursor cursor = database.query(MySQLiteHelper.TABLE_IDEAS,
                null, MySQLiteHelper.COLUMN_SERVER_ID+" = '"+server_id+"'", null, null, null, MySQLiteHelper.COLUMN_DATE+" desc");

        cursor.moveToFirst();

        if(cursor.getCount()>0)
            return true;
        else
            return false;

    }

    private Idea cursorToPost(Cursor cursor){
        Idea idea = new Idea();
        idea.setId(cursor.getLong(0));
        idea.setDate(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_DATE)));
        idea.setTitle(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_TITLE)));

        return idea;
    }

}
