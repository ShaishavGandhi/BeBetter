package shaishav.com.bebetter.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.heinrichreimersoftware.materialintro.slide.Slide;

import shaishav.com.bebetter.Fragments.IntroSecond;
import shaishav.com.bebetter.Fragments.IntroThird;
import shaishav.com.bebetter.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Intro extends IntroActivity {

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

        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .fragment(R.layout.intro_first, R.style.AppTheme)
                .build());

        FragmentSlide second = new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .fragment(new IntroSecond())
                .build();

        addSlide(second);

        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .fragment(new IntroThird())
                .build());

        addSlide(new SimpleSlide.Builder()
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .title("And you're done!")
                .description("Enjoying using BeBetter!")
                .build());




    }
}
