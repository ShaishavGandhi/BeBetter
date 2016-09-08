package shaishav.com.bebetter.Network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import shaishav.com.bebetter.Data.User;

/**
 * Created by Shaishav on 9/6/2016.
 */
public interface ApiEndPoint {

    @POST("users/")
    Call<User> loginUser(@Body User user);
}
