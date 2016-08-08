package shaishav.com.bebetter.Fragments;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

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

    PreferenceSource preferenceSource;

    Preference reminderTime,goal;
    SwitchPreference backup;
    String reminder_time_val,goal_val;
    boolean backup_val;

    public Settings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preferences);


        initialize();
        getPreferencesData();
        setPreferences();
        setPreferencesListeners();

    }

    public void initialize(){
        reminderTime = findPreference("reminderTime");
        backup = (SwitchPreference)findPreference("backup");
        goal = findPreference("goal");
        preferenceSource = PreferenceSource.getInstance(getActivity());
    }

    public void getPreferencesData(){

        reminder_time_val =  preferenceSource.getReminderTime();
        backup_val = preferenceSource.isBackupEnabled();
        goal_val = String.valueOf(preferenceSource.getGoal());

    }

    public void setPreferences(){

        reminderTime.setSummary(reminder_time_val);
        backup.setChecked(backup_val);
        goal.setSummary(goal_val + " min");

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
                preferenceSource.setIsBackupEnabled(!temp.isChecked());
                return true;
            }
        });

        goal.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new LovelyTextInputDialog(getActivity())
                        .setTopColorRes(R.color.colorPrimary)
                        .setTopTitle("Usage Goal")
                        .setTopTitleColor(Color.WHITE)
                        .setMessage("Enter your usage goal in min")
                        .setIcon(R.drawable.ic_smartphone_white_24dp)
                        .setInputType(InputType.TYPE_CLASS_NUMBER)
                        .setConfirmButton("Cool!", new LovelyTextInputDialog.OnTextInputConfirmListener() {
                            @Override
                            public void onTextInputConfirmed(String text) {
                                int min = Integer.parseInt(text);
                                goal.setSummary(text+" min");
                                preferenceSource.setGoal(min);
                            }

                        }).show();
                return true;
            }
        });

    }

    public void setTimeOnView(Preference reminderTime,int hourOfDay, int minute){

        reminderTime.setSummary(Constants.getTimeInAMPM(hourOfDay,minute));

    }



}
