package shaishav.com.bebetter.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import io.github.mthli.knife.KnifeText;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.Utils.IntentExtras;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ExperienceEditor extends AppCompatActivity {

    KnifeText editor;
    ImageButton bold;
    ImageButton italic;
    ImageButton underline;
    ImageButton strikethrough;
    ImageButton bullet;
    ImageButton quote;
    ImageButton link;

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
        setContentView(R.layout.activity_experience_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();

        setupListeners();

        Intent intent = getIntent();

        if(intent != null){
            String experienceString = intent.getStringExtra(IntentExtras.EXPERIENCE_STRING);
            editor.fromHtml(experienceString);

        }
    }

    private void initialize() {
        bold = (ImageButton) findViewById(R.id.bold);
        italic = (ImageButton) findViewById(R.id.italic);
        editor = (KnifeText)findViewById(R.id.editor);
        underline = (ImageButton) findViewById(R.id.underline);
        strikethrough = (ImageButton) findViewById(R.id.strikethrough);
        bullet = (ImageButton) findViewById(R.id.bullet);
        quote = (ImageButton) findViewById(R.id.quote);
    }

    private void setupListeners() {

        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.bold(!editor.contains(KnifeText.FORMAT_BOLD));
            }
        });

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.italic(!editor.contains(KnifeText.FORMAT_ITALIC));
            }
        });

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.underline(!editor.contains(KnifeText.FORMAT_UNDERLINED));
            }
        });

        strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.strikethrough(!editor.contains(KnifeText.FORMAT_STRIKETHROUGH));
            }
        });

        bullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.bullet(!editor.contains(KnifeText.FORMAT_BULLET));
            }
        });

        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.quote(!editor.contains(KnifeText.FORMAT_QUOTE));
            }
        });

    }

    private void setCheckedUncheckedState(boolean isChecked, ImageButton button) {
        if (isChecked) {
            button.setColorFilter(Color.BLUE, PorterDuff.Mode.LIGHTEN);
        } else {
            button.setColorFilter(Color.BLACK, PorterDuff.Mode.LIGHTEN);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra(IntentExtras.EXPERIENCE_STRING, editor.toHtml());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
