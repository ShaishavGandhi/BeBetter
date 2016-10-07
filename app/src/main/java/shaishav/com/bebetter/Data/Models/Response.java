package shaishav.com.bebetter.Data.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by Shaishav on 10/6/2016.
 */
public class Response {

    @SerializedName("action")
    String action;
    @SerializedName("response")
    Map<String, Object> response;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, Object> getResponse() {
        return response;
    }

    public void setResponse(Map<String, Object> response) {
        this.response = response;
    }
}
