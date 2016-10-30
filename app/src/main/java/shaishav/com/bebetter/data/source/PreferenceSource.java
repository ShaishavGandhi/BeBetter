package shaishav.com.bebetter.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import shaishav.com.bebetter.data.contracts.GoalContract;
import shaishav.com.bebetter.data.contracts.UsageContract;
import shaishav.com.bebetter.data.models.Goal;
import shaishav.com.bebetter.data.providers.GoalProvider;
import shaishav.com.bebetter.data.providers.UsageProvider;
import shaishav.com.bebetter.utils.App;
import shaishav.com.bebetter.utils.Constants;

/**
 * Created by Shaishav on 06-08-2016.
 */
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

    // Fired when phone unlocked
    public void savePhoneUnlockTime(){
        long last_unlocked = preferences.getLong(Constants.UNLOCKED,0);

        long unlocked = new Date().getTime();
        if(dayChanged(last_unlocked,unlocked) && last_unlocked!=0){
            storeSessionInDb(context,last_unlocked,preferences.getLong(Constants.SESSION,0));
            editor.putLong(Constants.SESSION,0);
        }


        editor.putLong(Constants.UNLOCKED, unlocked);
        editor.commit();
    }

    // Fired to record lock time of phone
    public void savePhoneLockTime(){
        long session_time;
        long lock_time = new Date().getTime();
        editor.putLong(Constants.LOCKED,lock_time);

        long last_unlock_time = preferences.getLong(Constants.UNLOCKED,0);

        if(last_unlock_time==0) {
            editor.commit();
            return;
        }

        if(dayChanged(lock_time,last_unlock_time)){
            Date previousDate = new Date(last_unlock_time);
            previousDate.setHours(23);
            previousDate.setMinutes(59);

            session_time = previousDate.getTime() - last_unlock_time;


            storeSessionInDb(context,previousDate.getTime(),preferences.getLong(Constants.SESSION,0)+session_time);

            previousDate.setDate(previousDate.getDate()+1);
            previousDate.setHours(0);
            previousDate.setMinutes(0);

            last_unlock_time = previousDate.getTime();

            session_time = lock_time - last_unlock_time;



            editor.putLong(Constants.UNLOCKED,last_unlock_time);
            editor.putLong(Constants.SESSION,session_time);

        }
        else{
            session_time = lock_time - last_unlock_time;
            long prev_session = preferences.getLong(Constants.SESSION,0);
            session_time += prev_session;
            editor.putLong(Constants.SESSION,session_time);
        }

        editor.commit();

    }

    public long getLastBackedUpTime(){
        return preferences.getLong(Constants.LAST_BACKED_UP,0);
    }

    public void setLastBackedUpTime(long time){
        editor.putLong(Constants.LAST_BACKED_UP, time);
        editor.commit();
    }

    public long getUsageUnit(){
        return preferences.getLong(Constants.PREFERENCE_USAGE_UNIT,1000*60);
    }

    public void setUsageUnit(String usageUnit){
        if(usageUnit.equals("Hours"))
            editor.putLong(Constants.PREFERENCE_USAGE_UNIT,1000*60*60);
        else
            editor.putLong(Constants.PREFERENCE_USAGE_UNIT,1000*60);
        editor.commit();
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

    public void setBackupPreference(int hourOfDay, int minute){

        editor.putInt(Constants.REMINDER_HOUR,hourOfDay);
        editor.putInt(Constants.REMINDER_MINUTE,minute);
        editor.commit();

        App app = new App();
        app.setReminder(context);
    }

    public String getReminderTime(){
        int hour = preferences.getInt(Constants.REMINDER_HOUR,0);
        int minute = preferences.getInt(Constants.REMINDER_MINUTE,0);
        return Constants.getTimeInAMPM(hour,minute);
    }

    public void saveUserData(String name, String email, String display_pic){
        editor.putString(Constants.FULL_NAME,name);
        editor.putString(Constants.EMAIL,email);
        editor.putString(Constants.DISPLAY_PIC,display_pic);
        editor.commit();
    }

    public String getName(){
        return preferences.getString(Constants.FULL_NAME,"");
    }

    public void setName(String name){
        editor.putString(Constants.FULL_NAME, name);
    }

    public String getEmail(){
        return preferences.getString(Constants.EMAIL,"");
    }

    public void saveReminderTime(int hour, int minute){
        editor.putInt(Constants.REMINDER_HOUR, hour);
        editor.putInt(Constants.REMINDER_MINUTE,minute);
        editor.commit();
    }

    public boolean isBackupEnabled(){
        return preferences.getBoolean(Constants.PREFERENCE_BACKUP,true);
    }

    public void setIsBackupEnabled(boolean backup){
        editor.putBoolean(Constants.PREFERENCE_BACKUP,backup);
        editor.commit();
    }

    public String getGcm(){
        return preferences.getString(Constants.GCM_TOKEN,"");
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

    public void setGoal(long date, long goal){
        saveGoalInDb(goal, date);
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

    public void setIsForegroundServiceRunning(boolean isRunning){
        editor.putBoolean(Constants.PREFERENCE_FOREGROUND,isRunning);
        editor.commit();
    }

    public boolean getIsForegroundServiceRunning(){
        return preferences.getBoolean(Constants.PREFERENCE_FOREGROUND,true);
    }

    // Helper functions start
    private boolean dayChanged(long lock_time,long unlock_time){

        Date lock_date = new Date(lock_time);
        Date unlock_date = new Date(unlock_time);

        setGoal(getPreviousDayGoal());

        return lock_date.getDate()!=unlock_date.getDate();

    }

    private long getPreviousDayGoal(){
        Goal goal = GoalSource.getPreviousDayGoal(context);
        if(goal != null){
            return goal.getGoal();
        }

        return 200*1000*60;
    }

    public void storeSessionInDb(Context context, long date, long session_time){
        ContentValues contentValues = new ContentValues();
        contentValues.put(UsageContract.COLUMN_DATE, date);
        contentValues.put(UsageContract.COLUMN_USAGE, session_time);
        context.getContentResolver().insert(UsageProvider.CONTENT_URI, contentValues);
    }

    // Helper functions end

}