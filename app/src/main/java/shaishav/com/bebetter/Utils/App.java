package shaishav.com.bebetter.Utils;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import shaishav.com.bebetter.Receiver.Reminder;
import shaishav.com.bebetter.Service.BackgroundService;

/**
 * Created by Shaishav on 22-06-2016.
 */
public class App extends Application {

    AlarmManager alarmManager;

    @Override
    public void onCreate(){
        super.onCreate();

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        // Set daily reminder
        setReminder();

        startService(new Intent(getApplicationContext(), BackgroundService.class));
    }

    public void setReminder(){

        Intent intent = new Intent(this,Reminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() ,24*60*60*1000,pendingIntent);

    }
}
