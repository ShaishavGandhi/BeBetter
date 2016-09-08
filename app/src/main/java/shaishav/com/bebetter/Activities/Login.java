package shaishav.com.bebetter.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import shaishav.com.bebetter.Data.PreferenceSource;
import shaishav.com.bebetter.Data.User;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.Network.ApiEndPoint;
import shaishav.com.bebetter.Network.ApiServiceLayer;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    final int RC_SIGN_IN = 0;
    PreferenceSource preferenceSource;
    GoogleApiClient mGoogleApiClient;
    ProgressDialog progressDialog;
    String display_pic;

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

        if(mGoogleApiClient.isConnected()){
            signOut();
        }

        //Set event listeners
        setEventListeners();

    }

    public void initialize(){
        preferenceSource = PreferenceSource.getInstance(getApplicationContext());
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
            final String gcm_token = FirebaseInstanceId.getInstance().getToken();
            display_pic = "";
            if(acct.getPhotoUrl() != null)
                display_pic = acct.getPhotoUrl().toString();

            final User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPhoto(display_pic);
            user.setGcm_id(gcm_token);

            ApiEndPoint client = ApiServiceLayer.createService(ApiEndPoint.class);

            Call<User> call = client.loginUser(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                    storeLocally(user.getName(), user.getEmail(), user.getPhoto());
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Oops! Couldn't log you in", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            // Signed out, show unauthenticated UI.
            progressDialog.hide();
            View view = findViewById(R.id.sign_in_button);
            Snackbar.make(view,"Couldn't sign in",Snackbar.LENGTH_SHORT).show();
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(getApplicationContext(),"Signed Out!",Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void storeLocally(String name,String email,String display_pic){
        preferenceSource.saveUserData(name,email,display_pic);

    }
}
