package shaishav.com.bebetter.Network.apis;

import java.util.List;
import java.util.Observable;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import shaishav.com.bebetter.Data.Models.Experience;
import shaishav.com.bebetter.Data.Models.Response;
import shaishav.com.bebetter.Data.Models.User;

/**
 * Created by Shaishav on 9/6/2016.
 */
public interface UserApiEndPoint {

    @POST("users/")
    Call<Object> loginUser(@Body User user);
}
