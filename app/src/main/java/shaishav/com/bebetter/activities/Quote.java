package shaishav.com.bebetter.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import shaishav.com.bebetter.R;

public class Quote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        String author = intent.getStringExtra("author");
        String quote = intent.getStringExtra("quote");

        CircleImageView imageView = (CircleImageView)findViewById(R.id.image);
        TextView quote_tv = (TextView)findViewById(R.id.quote);
        TextView author_tv = (TextView)findViewById(R.id.author);


        if(intent.hasExtra("image")) {
            Bitmap image = intent.getParcelableExtra("image");
            imageView.setImageBitmap(image);
        }

        quote_tv.setText(quote);
        author_tv.setText(author);

    }

}
