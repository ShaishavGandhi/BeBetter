package shaishav.com.bebetter.Network.apis;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import shaishav.com.bebetter.Data.models.Experience;

/**
 * Created by Shaishav on 10/4/2016.
 */
public interface ExperiencesApiEndPoint {

    @POST("lessons/")
    Call<Experience> createExperience(@Body Experience experience);

    @GET("lessons/")
    Call<List<Experience>> getExperiences();
}
