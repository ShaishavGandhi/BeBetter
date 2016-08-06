package shaishav.com.bebetter.Service;

import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;
import shaishav.com.bebetter.Utils.Notification;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){

        Map<String,String> data = remoteMessage.getData();
        Notification notification = new Notification();
        notification.createQuoteNotification(getApplicationContext(),data);
        Log.d("BeBetterGCM", "From: " + remoteMessage.getFrom());

    }


}
