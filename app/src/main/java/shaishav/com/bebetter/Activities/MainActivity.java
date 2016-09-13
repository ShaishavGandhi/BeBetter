package shaishav.com.bebetter.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shaishav.com.bebetter.Fragments.DaySummary;
import shaishav.com.bebetter.Fragments.LogOut;
import shaishav.com.bebetter.Fragments.Settings;
import shaishav.com.bebetter.Data.Models.Lesson;
import shaishav.com.bebetter.Data.Source.LessonSource;
import shaishav.com.bebetter.Fragments.LessonList;
import shaishav.com.bebetter.Network.ApiEndPoint;
import shaishav.com.bebetter.Network.ApiServiceLayer;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.Utils.Constants;
import shaishav.com.bebetter.Utils.NetworkRequests;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences preferences;
    LessonSource lessonSource;
    List<Lesson> lessonList;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddLesson.class);
                startActivity(intent);
            }
        });

        //Initialize everything in this activity
        initialize();

        if(isFirstTime())
            introduceApp();

        NetworkRequests requests = NetworkRequests.getInstance(getApplicationContext());

        ApiEndPoint apiEndPoint = ApiServiceLayer.createService(ApiEndPoint.class);
        Call<List<Lesson>> call = apiEndPoint.getLessons();
        call.enqueue(new Callback<List<Lesson>>() {
            @Override
            public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {
                List<Lesson> list = response.body();

                Toast.makeText(getApplicationContext(), list.size()+" size", Toast.LENGTH_SHORT).show();

                for(Lesson lesson : list){
                    Toast.makeText(getApplicationContext(), lesson.getTitle(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Lesson>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed because of "+t.toString(), Toast.LENGTH_LONG).show();
            }
        });
//        if(requests.isNetworkAvailable()) {
//            requests.getSyncedLessons();
//            requests.getSyncedUsages();
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String name = preferences.getString(Constants.FULL_NAME,"");
        String email = preferences.getString(Constants.EMAIL,"");
        String photo = preferences.getString(Constants.DISPLAY_PIC,"");


        TextView name_tv = (TextView)navigationView.getHeaderView(0).findViewById(R.id.name);
        TextView email_tv = (TextView)navigationView.getHeaderView(0).findViewById(R.id.email);

        name_tv.setText(name);
        email_tv.setText(email);

        if(!photo.equals(""))
        Picasso.with(getApplicationContext()).load(photo).into((ImageView)navigationView.getHeaderView(0).findViewById(R.id.imageView));


        //Set first screen
        setFirstScreen();

        String token = getGcmId();
        saveToken(token);

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
        lessonSource = new LessonSource(this);
        lessonSource.open();
        lessonList = lessonSource.getAllLessons();
        lessonSource.close();

        // Initialize preferences
        preferences = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;


        if (id == R.id.nav_daily_lessons)
            fragment = new LessonList();
        else if (id == R.id.nav_summary)
            fragment = new DaySummary();
        else if (id == R.id.nav_settings)
            fragment = new Settings();
        else if (id == R.id.nav_share)
            return true;
        else if (id == R.id.nav_send)
            return true;
        else if(id == R.id.log_out)
            fragment = new LogOut();


        fragmentManager.beginTransaction().replace(R.id.container_body,fragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
}
