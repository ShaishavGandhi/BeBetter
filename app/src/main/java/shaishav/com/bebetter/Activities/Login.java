package shaishav.com.bebetter.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;

import java.util.HashMap;
import java.util.Map;

import shaishav.com.bebetter.R;
import shaishav.com.bebetter.Utils.Constants;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    final int RC_SIGN_IN = 0;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    GoogleApiClient mGoogleApiClient;
    ProgressDialog progressDialog;

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
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize all variables
        initialize();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Set event listeners
        setEventListeners();

    }

    public void initialize(){
        //Preferences
        preferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        editor = preferences.edit();

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Initializing the awesome...");
    }

    public void setEventListeners(){

        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            progressDialog.show();
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            final String name = acct.getDisplayName();
            final String email = acct.getEmail();
            final String display_pic = acct.getPhotoUrl().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.HOST + Constants.USER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                    storeLocally(name,email,display_pic);

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    progressDialog.hide();
                    startActivity(intent);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put(Constants.POST_USER_NAME,name);
                    params.put(Constants.POST_USER_EMAIL,email);
                    params.put(Constants.POST_USER_PHOTO,display_pic);
                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {
            // Signed out, show unauthenticated UI.
            progressDialog.hide();
            View view = findViewById(R.id.sign_in_button);
            Snackbar.make(view,"Couldn't sign in",Snackbar.LENGTH_SHORT).show();
        }
    }

    public void storeLocally(String name,String email,String display_pic){
        editor.putString(Constants.FULL_NAME,name);
        editor.putString(Constants.EMAIL,email);
        editor.putString(Constants.DISPLAY_PIC,display_pic);
        editor.commit();
    }
}
