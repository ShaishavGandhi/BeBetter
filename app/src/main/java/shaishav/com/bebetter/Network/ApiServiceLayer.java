package shaishav.com.bebetter.Network;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import shaishav.com.bebetter.Data.Models.User;
import shaishav.com.bebetter.Network.api_layer.ExperiencesApiLayer;
import shaishav.com.bebetter.Network.api_layer.UsageApiLayer;
import shaishav.com.bebetter.Network.api_layer.UserApiLayer;
import shaishav.com.bebetter.Network.apis.UsageApiEndPoint;

/**
 * Created by Shaishav on 9/6/2016.
 */
public class ApiServiceLayer {

    public static final String BASE_URL = "https://bebetterserver.herokuapp.com/api/";
//    public static final String BASE_URL = "http://10.0.1.17:8080/api/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());


    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    public static void getBackedUpExperiences(Context context){
        ExperiencesApiLayer.getExperiences(context);
    }

    public static void loginUser(Context context, User user){
        UserApiLayer.loginUser(context, user);
    }

    public static void getBackedUpUsages(Context context, String email, long date){
        UsageApiLayer.getBackedUpUsages(context, email, date);
    }
}
