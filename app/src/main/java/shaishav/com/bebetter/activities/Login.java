package shaishav.com.bebetter.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
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

import java.util.Map;

import shaishav.com.bebetter.data.source.PreferenceSource;
import shaishav.com.bebetter.data.models.User;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.network.ApiServiceLayer;
import shaishav.com.bebetter.receiver.ApiResponseReceiver;
import shaishav.com.bebetter.network.ApiCallback;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, ApiCallback{

    final int RC_SIGN_IN = 0;
    PreferenceSource preferenceSource;
    GoogleApiClient mGoogleApiClient;
    ProgressDialog progressDialog;
    String display_pic;
    ApiResponseReceiver apiResponseReceiver;

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

        apiResponseReceiver = new ApiResponseReceiver(getApplicationContext(), this);

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
        IntentFilter intentFilter = new IntentFilter("loginUser");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(apiResponseReceiver, intentFilter);
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

            ApiServiceLayer.loginUser(getApplicationContext(), user);

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

    @Override
    public void onApiComplete(String action, Map<String, Object> args) {
        progressDialog.dismiss();
        if(action.equals("loginUser")){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        initialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(apiResponseReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(apiResponseReceiver);
        }
    }

    @Override
    public void onApiError(String action, int responseCode) {

    }
}
