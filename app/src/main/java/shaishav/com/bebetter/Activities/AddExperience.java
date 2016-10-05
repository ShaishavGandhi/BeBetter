package shaishav.com.bebetter.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import me.originqiu.library.EditTag;
import shaishav.com.bebetter.Data.Models.Experience;
import shaishav.com.bebetter.Data.Source.ExperienceSource;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.Utils.Constants;
import shaishav.com.bebetter.Network.NetworkRequests;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddExperience extends AppCompatActivity {

    EditText title,lesson;
    EditTag category;
    Switch isPublic;
    ImageButton tooltip;
    Button saveButton;
    ExperienceSource experienceSource;

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
        setContentView(R.layout.activity_add_lesson);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add an experience");
        setSupportActionBar(toolbar);

        // Add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        initialize();
        setEventListeners();

    }

    public void initialize(){

        //Initialize UI components
        title = (EditText)findViewById(R.id.title);
        lesson = (EditText)findViewById(R.id.lesson);
        category = (EditTag)findViewById(R.id.category);
        saveButton = (Button)findViewById(R.id.save);
        isPublic = (Switch)findViewById(R.id.make_public);
        tooltip = (ImageButton)findViewById(R.id.tooltip);
        tooltip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddExperience.this);
                alertDialogBuilder.setTitle("Share Your Experience.");
                alertDialogBuilder.setMessage("At BeBetter, we feel that everyone in the community will benefit if we all share " +
                        "our experiences. If your experience can benefit someone, consider making it public!");
                alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                alertDialogBuilder.show();

            }
        });

        //Database connection
        experienceSource = new ExperienceSource(this);

        title.getBackground().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        lesson.getBackground().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

    }



    public void setEventListeners(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButton.setEnabled(false);
                String titleText = title.getText().toString().trim();
                String lessonText = lesson.getText().toString().trim();
                List<String> catList = category.getTagList();
                boolean is_public = isPublic.isChecked();
                String categoryText = Constants.convertListToString(catList);

                if(titleText.length()>0 && lessonText.length()>0 && catList.size()>0) {
                    experienceSource.open();
                    Experience experience = experienceSource.createLesson(titleText,lessonText,categoryText,new Date().getTime(),is_public);
                    experienceSource.close();
                    NetworkRequests networkRequests = NetworkRequests.getInstance(getApplicationContext());
                    networkRequests.syncLesson(experience);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(),"Nice! Keep going!",Toast.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make(view,"Please check your input",Snackbar.LENGTH_LONG).show();
                }
            }
        });
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
