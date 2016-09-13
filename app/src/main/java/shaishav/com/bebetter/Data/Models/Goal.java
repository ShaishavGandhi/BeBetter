package shaishav.com.bebetter.Data.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shaishav on 9/5/2016.
 */
public class Goal {

    long id, date, goal;

    @SerializedName("_id")
    String server_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getGoal() {
        return goal;
    }

    public void setGoal(long goal) {
        this.goal = goal;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }
}
