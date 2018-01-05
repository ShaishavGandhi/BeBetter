package shaishav.com.bebetter.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shaishav on 26-06-2016.
 */
public class Usage {

    private long id;
    private long date;
    private long usage;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public void setUsage(long usage) {
        this.usage = usage;
    }

    public long getUsage() {
        return usage;
    }

}