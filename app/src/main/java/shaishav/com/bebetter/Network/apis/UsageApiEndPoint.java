package shaishav.com.bebetter.Network.apis;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import shaishav.com.bebetter.Data.Models.Response;
import shaishav.com.bebetter.Network.resource.UsageResponse;

/**
 * Created by Shaishav on 10/4/2016.
 */
public interface UsageApiEndPoint {

    @GET("usages")
    Call<UsageResponse> getBackedUpUsages(@Query("email") String email, @Query("date") long date);
}
