package shaishav.com.bebetter.Fragments;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

import shaishav.com.bebetter.R;
import shaishav.com.bebetter.Utils.App;
import shaishav.com.bebetter.Utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends PreferenceFragment {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public Settings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preferences);

        preferences = getActivity().getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE);
        editor = preferences.edit();

        final Preference reminderTime = (Preference) findPreference("reminderTime");
        String reminder_time =  getReminderTime();
        reminderTime.setSummary(reminder_time);
        
        reminderTime.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Calendar calendar = Calendar.getInstance();
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        setTimeOnView(reminderTime,hourOfDay,minute);
                        setBackupPreference(hourOfDay,minute);
                    }
                },calendar.getTime().getHours(),calendar.getTime().getMinutes(),false).show();

                return false;
            }
        });
    }

    private void setBackupPreference(int hourOfDay, int minute){
        editor.putInt(Constants.REMINDER_HOUR,hourOfDay);
        editor.putInt(Constants.REMINDER_MINUTE,minute);
        editor.commit();

        App app = new App();
        app.setReminder(getActivity().getApplicationContext());
    }

    public void setTimeOnView(Preference reminderTime,int hourOfDay, int minute){

        reminderTime.setSummary(Constants.getTimeInAMPM(hourOfDay,minute));
    }


    public String getReminderTime(){

        int hour = preferences.getInt(Constants.REMINDER_HOUR,0);
        int minute = preferences.getInt(Constants.REMINDER_MINUTE,0);

        return Constants.getTimeInAMPM(hour,minute);
    }


}
