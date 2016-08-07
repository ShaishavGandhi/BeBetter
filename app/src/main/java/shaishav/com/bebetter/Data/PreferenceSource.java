package shaishav.com.bebetter.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Date;

import shaishav.com.bebetter.Utils.App;
import shaishav.com.bebetter.Utils.Constants;

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

    public void setBackupPreference(int hourOfDay, int minute){

        editor.putInt(Constants.REMINDER_HOUR,hourOfDay);
        editor.putInt(Constants.REMINDER_MINUTE,minute);
        editor.commit();

        App app = new App();
        app.setReminder(context.getApplicationContext());
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

        return lock_date.getDate()!=unlock_date.getDate();

    }

    public void storeSessionInDb(Context context, long date, long session_time){
        UsageSource usageSource = new UsageSource(context);
        usageSource.open();
        usageSource.createUsage(date,session_time);
        usageSource.close();
    }

    // Helper functions end

}
