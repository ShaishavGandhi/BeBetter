package shaishav.com.bebetter.Utils;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

import shaishav.com.bebetter.Receiver.Backup;
import shaishav.com.bebetter.Receiver.Reminder;
import shaishav.com.bebetter.Service.BackgroundService;

/**
 * Created by Shaishav on 22-06-2016.
 */
public class App extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES,MODE_PRIVATE);
        if(preferences.getBoolean(Constants.FIRST_TIME,false)) {

            // Set daily reminder
            setReminder(this);
            setBackupSchedule(this);
            startService(new Intent(getApplicationContext(), BackgroundService.class));
        }
    }

    public void setBackupSchedule(Context context){
        Intent intent = new Intent(context,Backup.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND, 00);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() ,24*60*60*1000,pendingIntent);
    }

    public void setReminder(Context context){

        Intent intent = new Intent(context,Reminder.class);
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCES,MODE_PRIVATE);
        int hour = preferences.getInt(Constants.REMINDER_HOUR,21);
        int minute = preferences.getInt(Constants.REMINDER_MINUTE,0);
        if(!preferences.getBoolean(Constants.FIRST_TIME,false)) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 00);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
        }

    }
}
