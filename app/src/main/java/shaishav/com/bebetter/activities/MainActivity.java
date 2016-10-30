package shaishav.com.bebetter.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;
import java.util.Map;

import shaishav.com.bebetter.data.source.PreferenceSource;
import shaishav.com.bebetter.fragments.DaySummary;
import shaishav.com.bebetter.fragments.Settings;
import shaishav.com.bebetter.data.models.Experience;
import shaishav.com.bebetter.data.source.ExperienceSource;
import shaishav.com.bebetter.fragments.ExperienceList;
import shaishav.com.bebetter.network.ApiCallback;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.receiver.ApiResponseReceiver;
import shaishav.com.bebetter.utils.Constants;
import shaishav.com.bebetter.network.NetworkRequests;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener,
        ApiCallback {

    SharedPreferences preferences;
    ExperienceSource experienceSource;
    List<Experience> experienceList;
    ApiResponseReceiver apiResponseReceiver;
    PreferenceSource preferenceSource;
    BottomNavigationView mBottomNavigationView;
    FloatingActionButton fab;

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

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddExperience.class);
                startActivity(intent);
            }
        });

        //Initialize everything in this activity
        initialize();

        if(isFirstTime())
            introduceApp();

        final String temp_user_email = preferenceSource.getEmail();
        //ApiServiceLayer.getBackedUpUsages(MainActivity.this, temp_user_email, new Date().getTime());

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        String name = preferences.getString(Constants.FULL_NAME,"");
        String email = preferences.getString(Constants.EMAIL,"");
        String photo = preferences.getString(Constants.DISPLAY_PIC,"");

        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);

        mBottomNavigationView.setOnNavigationItemSelectedListener(this);


//        TextView name_tv = (TextView)navigationView.getHeaderView(0).findViewById(R.id.name);
//        TextView email_tv = (TextView)navigationView.getHeaderView(0).findViewById(R.id.email);
//
//        name_tv.setText(name);
//        email_tv.setText(email);
//
//        if(!photo.equals(""))
//        Picasso.with(getApplicationContext()).load(photo).into((ImageView)navigationView.getHeaderView(0).findViewById(R.id.imageView));


        //Set first screen
        setFirstScreen();

        String token = getGcmId();
        saveToken(token);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void saveToken(String token){
        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String existing_token = preferences.getString(Constants.GCM_TOKEN,"");

        if(!existing_token.equals(token)){
            editor.putString(Constants.GCM_TOKEN,token);
            editor.commit();

            NetworkRequests networkRequests = NetworkRequests.getInstance(getApplicationContext());
            if(networkRequests.isNetworkAvailable())
                networkRequests.updateGcmId(token);
        }

    }

    public void setFirstScreen(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_body,new DaySummary()).commit();

    }

    public void initialize(){

        // Open connection to Db and get all lessons
        experienceSource = new ExperienceSource(this);
        experienceSource.open();
        experienceList = experienceSource.getAllLessons();
        experienceSource.close();

        // Initialize preferences
        preferenceSource = PreferenceSource.getInstance(MainActivity.this);
        preferences = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);

        apiResponseReceiver = new ApiResponseReceiver(MainActivity.this, this);
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(apiResponseReceiver, new IntentFilter("getExperiences"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(apiResponseReceiver != null) {
            LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(apiResponseReceiver);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        fab.animate().translationY(fab.getHeight() + mBottomNavigationView.getHeight() + 100).setDuration(100);
    }

    public void showBottomNavigation(){
        mBottomNavigationView.animate().translationY(0).setDuration(100);
        fab.animate().translationY(0).setDuration(100);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;


        if (id == R.id.nav_daily_experiences)
            fragment = new ExperienceList();
        else if (id == R.id.nav_summary)
            fragment = new DaySummary();
        else if (id == R.id.nav_settings)
            fragment = new Settings();
//        else if (id == R.id.nav_share)
//            return true;
//        else if (id == R.id.nav_send)
//            return true;
//        else if(id == R.id.log_out)
//            fragment = new LogOut();

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

    public String getGcmId(){
        return FirebaseInstanceId.getInstance().getToken();

    }

    @Override
    public void onApiComplete(String action, Map<String, Object> args) {
        if(action.equals("getBackedUpUsages")){
            Toast.makeText(getApplicationContext(), "Got back usages", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onApiError(String action, int responseCode) {

    }
}
