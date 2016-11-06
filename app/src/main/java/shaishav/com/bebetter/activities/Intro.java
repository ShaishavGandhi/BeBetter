package shaishav.com.bebetter.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.paolorotolo.appintro.AppIntro;

import java.util.Date;

import shaishav.com.bebetter.data.source.PreferenceSource;
import shaishav.com.bebetter.fragments.IntroFirst;
import shaishav.com.bebetter.fragments.IntroSecond;
import shaishav.com.bebetter.fragments.IntroThird;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.service.BackgroundService;
import shaishav.com.bebetter.utils.App;
import shaishav.com.bebetter.utils.Constants;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Shaishav on 9/7/2016.
 */
public class Intro extends AppIntro {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("Exo2-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        addSlide(new IntroFirst());
        addSlide(new IntroSecond());
        addSlide(new IntroThird());

        showSkipButton(false);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        if(currentFragment instanceof IntroThird){
            View view = currentFragment.getView();
            EditText editText = (EditText)view.findViewById(R.id.goal);
            int goal = Integer.parseInt(editText.getText().toString());

            PreferenceSource preferenceSource = PreferenceSource.getInstance(getApplicationContext());
            preferenceSource.setGoal(goal*1000*60);

            App app = new App();

            startService(new Intent(getApplicationContext(), BackgroundService.class));
            SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES,MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(Constants.UNLOCKED,new Date().getTime());
            editor.commit();
        }

        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        PreferenceSource preferenceSource = PreferenceSource.getInstance(getApplicationContext());

        if(newFragment instanceof IntroThird){
            View view = oldFragment.getView();
            TextView time_tv = (TextView)view.findViewById(R.id.time);
            saveReminderTime(time_tv.getText().toString(), preferenceSource);

        }
        // Do something when the slide changes.
    }

    private void saveReminderTime(String time, PreferenceSource preferenceSource){
        int hour = Integer.parseInt(time.substring(0, time.indexOf(":")));
        int minute = Integer.parseInt(time.substring(time.indexOf(":")+1, time.indexOf(":")+3));

        preferenceSource.saveReminderTime(hour, minute);
    }

}
