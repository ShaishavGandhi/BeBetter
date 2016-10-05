package shaishav.com.bebetter.Network.api_layer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shaishav.com.bebetter.Data.Models.User;
import shaishav.com.bebetter.Data.Source.PreferenceSource;
import shaishav.com.bebetter.Network.ApiServiceLayer;
import shaishav.com.bebetter.Network.apis.UserApiEndPoint;

/**
 * Created by Shaishav on 10/4/2016.
 */
public class UserApiLayer {

    public static void loginUser(final Context context, final User user){
        UserApiEndPoint endPoint = ApiServiceLayer.createService(UserApiEndPoint.class);

        Call<User> call = endPoint.loginUser(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                PreferenceSource.getInstance(context).saveUserData(user.getName(), user.getEmail(), user.getPhoto());
                Intent intent = new Intent("loginUser");
                Log.v("bebetter", "Got resp from server");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

}
