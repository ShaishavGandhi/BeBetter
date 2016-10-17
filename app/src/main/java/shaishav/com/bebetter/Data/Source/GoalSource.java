package shaishav.com.bebetter.Data.Source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import shaishav.com.bebetter.Data.models.Goal;
import shaishav.com.bebetter.Data.MySQLiteHelper;

/**
 * Created by Shaishav on 9/5/2016.
 */
public class GoalSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private Context context;



    public GoalSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Goal createGoal(long date, long goal){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteHelper.COLUMN_DATE,date);
        contentValues.put(MySQLiteHelper.COLUMN_GOAL, goal);

        long insertId = database.insert(MySQLiteHelper.TABLE_GOAL, null, contentValues);



        Cursor cursor = database.query(MySQLiteHelper.TABLE_GOAL,null,MySQLiteHelper.COLUMN_ID+"="+insertId,null,null,null
                ,MySQLiteHelper.COLUMN_ID+" desc");

        cursor.moveToFirst();



        Goal goalObj = cursorToPost(cursor);
        cursor.close();

        return goalObj;
    }

    public Goal getCurrentGoal(){

        Date date = new Date();
        date.setHours(23);
        date.setMinutes(59);
        long high = date.getTime();

        date.setHours(0);
        date.setMinutes(0);
        long low = date.getTime();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_GOAL,
                null, MySQLiteHelper.COLUMN_DATE + " > "+low+" AND "+MySQLiteHelper.COLUMN_DATE+" < "+high,
                null, null, null, MySQLiteHelper.COLUMN_DATE+" ASC");

        cursor.moveToFirst();

        if(cursor.getCount() > 0) {
            Goal goal = cursorToPost(cursor);
            cursor.close();
            return goal;
        }
        return null;

    }

    public Goal getPreviousDayGoal(){

        Date date = new Date();
        date.setDate(date.getDate() - 1);
        date.setHours(23);
        date.setMinutes(59);
        long high = date.getTime();

        date.setHours(0);
        date.setMinutes(0);
        long low = date.getTime();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_GOAL,
                null, MySQLiteHelper.COLUMN_DATE + " > "+low+" AND "+MySQLiteHelper.COLUMN_DATE+" < "+high,
                null, null, null, MySQLiteHelper.COLUMN_DATE+" ASC");

        cursor.moveToFirst();

        if(cursor.getCount() > 0) {
            Goal goal = cursorToPost(cursor);
            cursor.close();
            return goal;
        }
        return null;

    }

    public boolean goalAlreadyExists(long date){
        Date dt = new Date(date);
        dt.setHours(23);
        dt.setMinutes(59);
        long high = dt.getTime();

        dt.setHours(0);
        dt.setMinutes(0);
        long low = dt.getTime();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_GOAL,
                null, MySQLiteHelper.COLUMN_DATE + " > "+low+" AND "+MySQLiteHelper.COLUMN_DATE+" < "+high,
                null, null, null, MySQLiteHelper.COLUMN_DATE+" ASC");

        cursor.moveToFirst();

        return (cursor.getCount() > 0);
    }

    public List<Goal> getAllUsages(){
        List<Goal> goals = new ArrayList<Goal>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_USAGE,
                null, null, null, null, null, MySQLiteHelper.COLUMN_ID+" desc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Goal goal = cursorToPost(cursor);
            goals.add(goal);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return goals;
    }

    public List<Goal> getData(long lower_threshold,long higher_threshold){

        List<Goal> goals = new ArrayList<Goal>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_GOAL,
                null, MySQLiteHelper.COLUMN_DATE + " > "+lower_threshold+" AND "+MySQLiteHelper.COLUMN_DATE+" < "+higher_threshold,
                null, null, null, MySQLiteHelper.COLUMN_DATE+" ASC");

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Goal goal = cursorToPost(cursor);
            goals.add(goal);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return goals;

    }

    public List<Goal> getGoalsForBackup(){
        List<Goal> goals = new ArrayList<Goal>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_GOAL,
                null, MySQLiteHelper.COLUMN_SERVER_ID+" = 'NA'", null, null, null, MySQLiteHelper.COLUMN_DATE+" asc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Goal goal = cursorToPost(cursor);
            goals.add(goal);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return goals;
    }

    public void setServerId(String server_id, int id){
        ContentValues cv = new ContentValues();
        cv.put(MySQLiteHelper.COLUMN_SERVER_ID,server_id);

        database.update(MySQLiteHelper.TABLE_GOAL,cv,MySQLiteHelper.COLUMN_ID+" = "+id,null);
    }


    public boolean isExisting(String server_id){

        Cursor cursor = database.query(MySQLiteHelper.TABLE_GOAL,
                null, MySQLiteHelper.COLUMN_SERVER_ID+" = '"+server_id+"'", null, null, null, MySQLiteHelper.COLUMN_DATE+" desc");

        cursor.moveToFirst();

        if(cursor.getCount()>0)
            return true;
        else
            return false;

    }


    private Goal cursorToPost(Cursor cursor){
        Goal goal = new Goal();
        goal.setId(cursor.getLong(0));
        goal.setDate(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_DATE)));
        goal.setGoal(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_GOAL)));
        goal.setServer_id(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SERVER_ID)));

        return goal;
    }

}
