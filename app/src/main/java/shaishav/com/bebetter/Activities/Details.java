package shaishav.com.bebetter.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import me.originqiu.library.EditTag;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.Utils.Constants;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Details extends AppCompatActivity {

    private String title,content;
    private List<String> category;
    private Date created_at;
    private long id;

    private TextView title_et,content_et,date_et;
    private EditTag category_et;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("Exo2-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        initialize();

        //Get all data from intent
        getIntentData(getIntent());

        //Set toolbar title
        toolbar.setTitle(title);

        setSupportActionBar(toolbar);

        // Add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Bind Data
        bindData();

    }

    public void initialize(){
        title_et = (TextView)findViewById(R.id.title);
        category_et = (EditTag) findViewById(R.id.category);
        date_et = (TextView)findViewById(R.id.created_at);
        content_et = (TextView)findViewById(R.id.content);

    }

    public void bindData(){
        title_et.setText(title);
        category_et.setEditable(false);
        //category_et.setTagList(category);
        String dateString = Constants.getFormattedDate(created_at);
        date_et.setText(dateString);
        content_et.setText(Html.fromHtml(content));
    }



    private void getIntentData(Intent intent){
        title = intent.getStringExtra("title");
        String cats = intent.getStringExtra("category");
        //category = Arrays.asList(cats.split(","));
        content = intent.getStringExtra("content");

        created_at = new Date(intent.getLongExtra("created_at",0));
        id = intent.getLongExtra("id",0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
