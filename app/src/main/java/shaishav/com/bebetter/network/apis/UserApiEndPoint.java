package shaishav.com.bebetter.network.apis;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import shaishav.com.bebetter.data.models.User;

/**
 * Created by Shaishav on 9/6/2016.
 */
public interface UserApiEndPoint {

    @POST("users/")
    Call<Object> loginUser(@Body User user);
}
