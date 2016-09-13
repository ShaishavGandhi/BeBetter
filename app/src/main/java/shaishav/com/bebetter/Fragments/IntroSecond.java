package shaishav.com.bebetter.Fragments;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;


import java.util.Calendar;

import shaishav.com.bebetter.Data.Source.PreferenceSource;
import shaishav.com.bebetter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroSecond extends Fragment {


    public static TextView time_tv;
    public static Context context;
    static PreferenceSource preferenceSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.intro_second, container, false);
        context = getActivity().getApplicationContext();
        preferenceSource = PreferenceSource.getInstance(context);

        Button button = (Button)rootView.findViewById(R.id.reminder_time);
        time_tv = (TextView)rootView.findViewById(R.id.time);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
            }
        });

        return rootView;
    }


    public static void setReminderTime(int hourOfDay,int minute){
        preferenceSource.saveReminderTime(hourOfDay, minute);
    }

     public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String AM_PM;
            String time = "";
            int hour = hourOfDay;
            if (hour < 12)
                AM_PM = "AM";
            else {
                AM_PM = "PM";
                hour -= 12;
            }

            if (hour < 10)
                time += "0" + hour + ":";
            else
                time += hour + ":";
            if (minute < 10)
                time += "0" + minute;
            else
                time += minute;

            time += " " + AM_PM;

            time_tv.setText(time);

            setReminderTime(hourOfDay,minute);
        }
    }

}


