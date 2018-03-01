package shaishav.com.bebetter.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;
import java.util.List;

import shaishav.com.bebetter.data.contracts.GoalContract;
import shaishav.com.bebetter.data.contracts.PointContract;
import shaishav.com.bebetter.data.contracts.UsageContract;
import shaishav.com.bebetter.data.models.Goal;
import shaishav.com.bebetter.data.models.Usage;
import shaishav.com.bebetter.data.providers.GoalProvider;
import shaishav.com.bebetter.data.providers.UsageProvider;
import shaishav.com.bebetter.utils.Constants;

/**
 * Created by Shaishav on 06-08-2016.
 */
@Deprecated
public class PreferenceSource {

    public static PreferenceSource preferenceSource;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    protected PreferenceSource(Context context){
        preferences = context.getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE);
        editor = preferences.edit();
        this.context = context;
    }

    public static PreferenceSource getInstance(Context context){
        if(preferenceSource==null){
            preferenceSource = new PreferenceSource(context);
        }

        return preferenceSource;
    }


    public long getUsageUnit(){
        return preferences.getLong(Constants.PREFERENCE_USAGE_UNIT, 1000*60);
    }

    public long getLastUnlockedTime(){
        return preferences.getLong(Constants.UNLOCKED,0);
    }

    public long getLastLockedTime(){
        return preferences.getLong(Constants.LOCKED,0);
    }

    public long getSessionTime(){
        return preferences.getLong(Constants.SESSION, 0);
    }

    public String getName(){
        return preferences.getString(Constants.FULL_NAME,"");
    }

    public void setName(String name){
        editor.putString(Constants.FULL_NAME, name);
    }

    public long getGoal(){

        Goal goal = GoalSource.getCurrentGoal(context);
        if(goal!=null) {
            return goal.getGoal();
        }
        return 200*1000*60;
    }

    public void setGoal(long goal){
        Date date = new Date();
        saveGoalInDb(goal, date.getTime());
        editor.putLong(Constants.GOAL,goal);
        editor.commit();
    }

    private void saveGoalInDb(long goal, long date){
        if(!GoalSource.goalAlreadyExists(context, date)){
            //goalSource.createGoal(date, goal);
            ContentValues values = new ContentValues();
            values.put(GoalContract.COLUMN_DATE, date);
            values.put(GoalContract.COLUMN_GOAL, goal);
            context.getContentResolver().insert(GoalProvider.CONTENT_URI, values);
        }
    }

    // Helper functions end

}
