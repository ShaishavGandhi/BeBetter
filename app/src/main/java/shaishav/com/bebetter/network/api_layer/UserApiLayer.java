package shaishav.com.bebetter.network.api_layer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shaishav.com.bebetter.data.models.User;
import shaishav.com.bebetter.data.source.PreferenceSource;
import shaishav.com.bebetter.network.ApiServiceLayer;
import shaishav.com.bebetter.network.apis.UserApiEndPoint;
import shaishav.com.bebetter.utils.ApiExtras;

/**
 * Created by Shaishav on 10/4/2016.
 */
public class UserApiLayer {

    public static void loginUser(final Context context, final User user){
        UserApiEndPoint endPoint = ApiServiceLayer.createService(UserApiEndPoint.class);

        Call<Object> call = endPoint.loginUser(user);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                PreferenceSource.getInstance(context).saveUserData(user.getName(), user.getEmail(), user.getPhoto());
                Intent intent = new Intent("loginUser");

                Map<String, Object> map = (Map<String, Object>)response.body();

                JsonElement jsonElement = new Gson().toJsonTree(map.get(ApiExtras.RESPONSE));
                User newUser = new Gson().fromJson(jsonElement, User.class);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

}
