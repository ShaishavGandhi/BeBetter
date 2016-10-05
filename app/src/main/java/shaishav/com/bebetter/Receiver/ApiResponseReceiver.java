package shaishav.com.bebetter.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import shaishav.com.bebetter.Network.ApiCallback;

/**
 * Created by Shaishav on 10/4/2016.
 */
public class ApiResponseReceiver extends BroadcastReceiver {

    private Context mContext;
    private ApiCallback mApiCallback;

    public ApiResponseReceiver(Context context, ApiCallback apiCallback){
        this.mContext = context;
        this.mApiCallback = apiCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("loginUser")){
            mApiCallback.onApiComplete(intent.getAction(), null);
        }
    }
}
