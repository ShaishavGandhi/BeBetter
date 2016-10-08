package shaishav.com.bebetter.Network.api_layer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import shaishav.com.bebetter.Data.Models.Response;
import shaishav.com.bebetter.Data.Models.Usage;
import shaishav.com.bebetter.Data.Source.UsageSource;
import shaishav.com.bebetter.Network.ApiServiceLayer;
import shaishav.com.bebetter.Network.apis.UsageApiEndPoint;
import shaishav.com.bebetter.Network.resource.UsageResponse;

/**
 * Created by Shaishav on 10/7/2016.
 */
public class UsageApiLayer {

    public static void getBackedUpUsages(final Context context, String email, long date){
        UsageApiEndPoint endPoint = ApiServiceLayer.createService(UsageApiEndPoint.class);

        Call<UsageResponse> call = endPoint.getBackedUpUsages(email, date);
        call.enqueue(new Callback<UsageResponse>() {
            @Override
            public void onResponse(Call<UsageResponse> call, retrofit2.Response<UsageResponse> response) {
                List<Usage> usages = response.body().getUsages();
                if(usages != null){
                    UsageSource usageSource = new UsageSource(context);
                    usageSource.open();
                    for(int i = 0; i < usages.size(); i++){
                        Usage usage = usages.get(i);
                        if(!usageSource.isExisting(usage.getServer_id())){
                           usageSource.createUsage(usage.getDate(), usage.getUsage());
                        }
                    }
                    usageSource.close();
                }
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("getBackedUpUsages"));
            }

            @Override
            public void onFailure(Call<UsageResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
