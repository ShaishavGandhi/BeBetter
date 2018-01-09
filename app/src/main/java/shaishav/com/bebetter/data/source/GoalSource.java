package shaishav.com.bebetter.data.source;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import shaishav.com.bebetter.data.MySQLiteHelper;
import shaishav.com.bebetter.data.models.Goal;
import shaishav.com.bebetter.data.providers.GoalProvider;

/**
 * Created by Shaishav on 9/5/2016.
 */
@Deprecated
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
            Goal goal = GoalProvider.cursorToGoal(cursor);
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

        cursor.moveToFirst();

        if(cursor.getCount() > 0) {
            Goal goal = GoalProvider.cursorToGoal(cursor);
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

    public static List<Goal> getAllGoals(Context context) {
        Cursor cursor = context.getContentResolver().query(GoalProvider.CONTENT_URI, null, null, null,
                GoalProvider.QUERY_SORT_ORDER);

        List<Goal> goals = GoalProvider.cursorToListGoals(cursor);
        cursor.close();
        return goals;
    }

    public static List<Goal> getData(Context context, long lower_threshold, long higher_threshold){

        List<Goal> goals = new ArrayList<Goal>();

        String[] whereClause = new String[]{String.valueOf(lower_threshold), String.valueOf(higher_threshold)};

        Cursor cursor = context.getContentResolver().query(GoalProvider.CONTENT_URI, null, GoalProvider.QUERY_SELECTION_ARGS_GOAL_RANGE,
                whereClause, GoalProvider.QUERY_SORT_ORDER);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Goal goal = GoalProvider.cursorToGoal(cursor);
            goals.add(goal);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return goals;

    }

}
