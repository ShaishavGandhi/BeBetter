package shaishav.com.bebetter.data.source;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import shaishav.com.bebetter.data.models.Goal;
import shaishav.com.bebetter.data.MySQLiteHelper;
import shaishav.com.bebetter.data.providers.GoalProvider;

/**
 * Created by Shaishav on 9/5/2016.
 */
public class GoalSource {

    public static Goal getCurrentGoal(Context context){

        Date date = new Date();
        date.setHours(23);
        date.setMinutes(59);
        long high = date.getTime();

        date.setHours(0);
        date.setMinutes(0);
        long low = date.getTime();

        String[] whereClause = new String[]{String.valueOf(low), String.valueOf(high)};

        Cursor cursor = context.getContentResolver().query(GoalProvider.CONTENT_URI, null, GoalProvider.QUERY_SELECTION_ARGS_GOAL_RANGE,
                whereClause, GoalProvider.QUERY_SORT_ORDER);

        cursor.moveToFirst();

        if(cursor.getCount() > 0) {
            Goal goal = cursorToPost(cursor);
            cursor.close();
            return goal;
        }
        return null;

    }

    public static Goal getPreviousDayGoal(Context context){

        Date date = new Date();
        date.setDate(date.getDate() - 1);
        date.setHours(23);
        date.setMinutes(59);
        long high = date.getTime();

        date.setHours(0);
        date.setMinutes(0);
        long low = date.getTime();

        String[] whereClause = new String[]{String.valueOf(low), String.valueOf(high)};

        Cursor cursor = context.getContentResolver().query(GoalProvider.CONTENT_URI, null, GoalProvider.QUERY_SELECTION_ARGS_GOAL_RANGE,
                whereClause, GoalProvider.QUERY_SORT_ORDER);

//        Cursor cursor = database.query(GoalContract.TABLE_GOAL,
//                null, MySQLiteHelper.COLUMN_DATE + " > "+low+" AND "+MySQLiteHelper.COLUMN_DATE+" < "+high,
//                null, null, null, MySQLiteHelper.COLUMN_DATE+" ASC");

        cursor.moveToFirst();

        if(cursor.getCount() > 0) {
            Goal goal = cursorToPost(cursor);
            cursor.close();
            return goal;
        }
        return null;

    }

    public static boolean goalAlreadyExists(Context context, long date){
        Date dt = new Date(date);
        dt.setHours(23);
        dt.setMinutes(59);
        long high = dt.getTime();

        dt.setHours(0);
        dt.setMinutes(0);
        long low = dt.getTime();

        String[] whereClause = new String[]{String.valueOf(low), String.valueOf(high)};

        Cursor cursor = context.getContentResolver().query(GoalProvider.CONTENT_URI, null, GoalProvider.QUERY_SELECTION_ARGS_GOAL_RANGE,
                whereClause, GoalProvider.QUERY_SORT_ORDER);

        cursor.moveToFirst();

        return (cursor.getCount() > 0);
    }

    public static List<Goal> getData(Context context, long lower_threshold, long higher_threshold){

        List<Goal> goals = new ArrayList<Goal>();

        String[] whereClause = new String[]{String.valueOf(lower_threshold), String.valueOf(higher_threshold)};

        Cursor cursor = context.getContentResolver().query(GoalProvider.CONTENT_URI, null, GoalProvider.QUERY_SELECTION_ARGS_GOAL_RANGE,
                whereClause, GoalProvider.QUERY_SORT_ORDER);

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

//    public List<Goal> getGoalsForBackup(){
//        List<Goal> goals = new ArrayList<Goal>();
//
//        Cursor cursor = database.query(GoalContract.TABLE_GOAL,
//                null, MySQLiteHelper.COLUMN_SERVER_ID+" = 'NA'", null, null, null, MySQLiteHelper.COLUMN_DATE+" asc");
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            Goal goal = cursorToPost(cursor);
//            goals.add(goal);
//            cursor.moveToNext();
//        }
//        // make sure to close the cursor
//        cursor.close();
//        return goals;
//    }
//
//    public void setServerId(String server_id, int id){
//        ContentValues cv = new ContentValues();
//        cv.put(MySQLiteHelper.COLUMN_SERVER_ID,server_id);
//
//        database.update(GoalContract.TABLE_GOAL,cv,MySQLiteHelper.COLUMN_ID+" = "+id,null);
//    }
//
//
//    public boolean isExisting(String server_id){
//
//        Cursor cursor = database.query(GoalContract.TABLE_GOAL,
//                null, MySQLiteHelper.COLUMN_SERVER_ID+" = '"+server_id+"'", null, null, null, MySQLiteHelper.COLUMN_DATE+" desc");
//
//        cursor.moveToFirst();
//
//        if(cursor.getCount()>0)
//            return true;
//        else
//            return false;
//
//    }


    private static Goal cursorToPost(Cursor cursor){
        Goal goal = new Goal();
        goal.setId(cursor.getLong(0));
        goal.setDate(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_DATE)));
        goal.setGoal(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_GOAL)));
        goal.setServer_id(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SERVER_ID)));
        return goal;
    }

}
