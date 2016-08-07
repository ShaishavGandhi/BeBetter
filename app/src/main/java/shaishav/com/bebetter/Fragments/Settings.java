package shaishav.com.bebetter.Fragments;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

import shaishav.com.bebetter.Data.PreferenceSource;
import shaishav.com.bebetter.Data.UsageSource;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.Service.BackgroundService;
import shaishav.com.bebetter.Utils.App;
import shaishav.com.bebetter.Utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends PreferenceFragment {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    PreferenceSource preferenceSource;

    Preference reminderTime;
    SwitchPreference backup;
    String reminder_time_val;
    boolean backup_val;

    public Settings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preferences);
        preferences = getActivity().getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE);
        editor = preferences.edit();

        initialize();
        getPreferencesData();
        setPreferences();
        setPreferencesListeners();

    }

    public void initialize(){
        reminderTime = findPreference("reminderTime");
        backup = (SwitchPreference)findPreference("backup");

        preferenceSource = PreferenceSource.getInstance(getActivity());
    }

    public void getPreferencesData(){

        reminder_time_val =  getReminderTime();
        backup_val = preferences.getBoolean(Constants.PREFERENCE_BACKUP,true);

    }

    public void setPreferences(){

        reminderTime.setSummary(reminder_time_val);
        backup.setChecked(backup_val);

    }

    public void setPreferencesListeners(){

        reminderTime.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Calendar calendar = Calendar.getInstance();
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        setTimeOnView(reminderTime,hourOfDay,minute);
                        preferenceSource.setBackupPreference(hourOfDay,minute);
                    }
                },calendar.getTime().getHours(),calendar.getTime().getMinutes(),false).show();

                return false;
            }
        });

        backup.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                SwitchPreference temp = (SwitchPreference)preference;
                editor.putBoolean(Constants.PREFERENCE_BACKUP,!temp.isChecked());
                editor.commit();
                return true;
            }
        });


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
