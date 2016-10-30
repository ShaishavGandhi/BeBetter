package shaishav.com.bebetter.fragments;


import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.Calendar;
import java.util.Date;

import shaishav.com.bebetter.data.source.PreferenceSource;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends PreferenceFragment {

    PreferenceSource preferenceSource;

    Preference reminderTime,goal;
    SwitchPreference backup;
    long usage_unit_val;
    String reminder_time_val,goal_val;
    boolean backup_val;
    String usageUnitText;

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

        if(preferenceSource.getUsageUnit() == 1000*60)
            usageUnitText = "min";
        else
            usageUnitText = "hours";
    }

    public void getPreferencesData(){

        reminder_time_val =  preferenceSource.getReminderTime();
        backup_val = preferenceSource.isBackupEnabled();
        goal_val = String.valueOf(preferenceSource.getGoal()/(preferenceSource.getUsageUnit()));
        usage_unit_val = preferenceSource.getUsageUnit();

    }

    public void setPreferences(){

        reminderTime.setSummary(reminder_time_val);
        backup.setChecked(backup_val);
        goal.setSummary(goal_val + " "+usageUnitText);


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
            public boolean onPreferenceClick(final Preference preference) {
                new LovelyTextInputDialog(getActivity())
                        .setTopColorRes(R.color.colorPrimary)
                        .setTopTitle("Usage Goal")
                        .setTopTitleColor(Color.WHITE)
                        .setMessage("Enter your usage goal in "+usageUnitText)
                        .setIcon(R.drawable.ic_smartphone_white_24dp)
                        .setInputType(InputType.TYPE_CLASS_NUMBER)
                        .setConfirmButtonColor(R.color.colorPrimaryDark)
                        .setConfirmButton("Cool!", new LovelyTextInputDialog.OnTextInputConfirmListener() {
                            @Override
                            public void onTextInputConfirmed(String text) {
                                if(!text.equals("")) {
                                    int min = Integer.parseInt(text);
                                    goal.setSummary(text + " "+usageUnitText);
                                    Date date = new Date();
                                    date.setDate(date.getDate() + 1);
                                    preferenceSource.setGoal(date.getTime(), min*preferenceSource.getUsageUnit());
                                    Toast.makeText(getActivity(), "Goal changed to " + text + " "+usageUnitText+"!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getActivity(),"Please enter your usage goal",Toast.LENGTH_SHORT).show();
                                }
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