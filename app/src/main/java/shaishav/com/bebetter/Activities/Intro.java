package shaishav.com.bebetter.Activities;

import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import shaishav.com.bebetter.R;

public class Intro extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .fragment(R.layout.activity_splash_screen, R.style.AppTheme)
                .build());
    }
}
