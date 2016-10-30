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
import shaishav.com.bebetter.custom.ImageSwitch;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ExperienceEditor extends AppCompatActivity {

    KnifeText editor;
    ImageSwitch bold;
    ImageSwitch italic;
    ImageSwitch underline;
    ImageSwitch strikethrough;
    ImageSwitch bullet;
    ImageSwitch quote;
    ImageSwitch link;

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
        bold = (ImageSwitch) findViewById(R.id.bold);
        italic = (ImageSwitch) findViewById(R.id.italic);
        editor = (KnifeText)findViewById(R.id.editor);
        underline = (ImageSwitch) findViewById(R.id.underline);
        strikethrough = (ImageSwitch) findViewById(R.id.strikethrough);
        bullet = (ImageSwitch) findViewById(R.id.bullet);
        quote = (ImageSwitch) findViewById(R.id.quote);
    }

    private void setupListeners() {

        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.bold(!editor.contains(KnifeText.FORMAT_BOLD));
                bold.toggleCheck();
            }
        });

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.italic(!editor.contains(KnifeText.FORMAT_ITALIC));
                italic.toggleCheck();
            }
        });

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.underline(!editor.contains(KnifeText.FORMAT_UNDERLINED));
                underline.toggleCheck();
            }
        });

        strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.strikethrough(!editor.contains(KnifeText.FORMAT_STRIKETHROUGH));
                strikethrough.toggleCheck();
            }
        });

        bullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.bullet(!editor.contains(KnifeText.FORMAT_BULLET));
                bullet.toggleCheck();
            }
        });

        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.quote(!editor.contains(KnifeText.FORMAT_QUOTE));
                quote.toggleCheck();
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
