package shaishav.com.bebetter.Network;

import java.util.Map;

/**
 * Created by Shaishav on 10/4/2016.
 */
public interface ApiCallback {

    public void onApiComplete(String action, Map<String, Object> args);

    public void onApiError(String action, int responseCode);
}
