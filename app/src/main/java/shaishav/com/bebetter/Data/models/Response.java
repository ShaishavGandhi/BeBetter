package shaishav.com.bebetter.Data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shaishav on 10/6/2016.
 */
public class Response {

    @SerializedName("action")
    String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
