package shaishav.com.bebetter.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import shaishav.com.bebetter.data.source.PreferenceSource;
import shaishav.com.bebetter.fragments.DaySummary;
import shaishav.com.bebetter.fragments.Leaderboard;
import shaishav.com.bebetter.fragments.Settings;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.utils.Constants;
import shaishav.com.bebetter.utils.FirebaseHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    SharedPreferences preferences;
    PreferenceSource preferenceSource;
    BottomNavigationView mBottomNavigationView;

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

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize everything in this activity
        initialize();

        if(isFirstTime())
            introduceApp();

        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);

        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        //Set first screen
        setFirstScreen();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setFirstScreen(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_body,new DaySummary()).commit();

    }

    public void initialize(){
        // Initialize preferences
        preferenceSource = PreferenceSource.getInstance(MainActivity.this);
        preferences = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void hideBottomNavigation(){
        mBottomNavigationView.animate().translationY(mBottomNavigationView.getHeight()).setDuration(100);
    }

    public void showBottomNavigation(){
        mBottomNavigationView.animate().translationY(0).setDuration(100);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;


        if (id == R.id.nav_daily_experiences)
            fragment = new Leaderboard();
        else if (id == R.id.nav_summary)
            fragment = new DaySummary();
        else if (id == R.id.nav_settings)
            fragment = new Settings();

        fragmentManager.beginTransaction().replace(R.id.container_body,fragment).commit();
        return true;
    }

    private boolean isFirstTime(){
        return !preferences.getBoolean(Constants.FIRST_TIME,false);
    }

    private void introduceApp(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.FIRST_TIME, true);
        editor.commit();

        Intent intent = new Intent(this, Intro.class);
        startActivity(intent);
    }
}
