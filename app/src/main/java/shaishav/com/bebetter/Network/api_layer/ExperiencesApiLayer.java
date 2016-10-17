package shaishav.com.bebetter.Network.api_layer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shaishav.com.bebetter.Data.models.Experience;
import shaishav.com.bebetter.Data.Source.ExperienceSource;
import shaishav.com.bebetter.Network.ApiServiceLayer;
import shaishav.com.bebetter.Network.apis.ExperiencesApiEndPoint;

/**
 * Created by Shaishav on 10/4/2016.
 */
public class ExperiencesApiLayer {

    public static void getExperiences(final Context context){
        ExperiencesApiEndPoint endPoint = ApiServiceLayer.createService(ExperiencesApiEndPoint.class);
        Call<List<Experience>> call = endPoint.getExperiences();
        call.enqueue(new Callback<List<Experience>>() {
            @Override
            public void onResponse(Call<List<Experience>> call, Response<List<Experience>> response) {
                List<Experience> experiences = response.body();
                ExperienceSource experienceSource = new ExperienceSource(context);
                experienceSource.open();
                for(Experience experience : experiences){
                    experienceSource.createLesson(experience.getTitle(), experience.getLesson(), experience.getCategory(),
                            experience.getCreated_at(), experience.getIs_public() == 1);
                }
                experienceSource.close();

                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("getExperiences"));
            }

            @Override
            public void onFailure(Call<List<Experience>> call, Throwable t) {

            }
        });
    }
}
