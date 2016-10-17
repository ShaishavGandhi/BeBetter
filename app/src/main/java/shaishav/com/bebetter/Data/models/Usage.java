package shaishav.com.bebetter.Data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shaishav on 26-06-2016.
 */
public class Usage {

    long id,date,usage;

    @SerializedName("_id")
    String server_id;

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public void setDate(long date){this.date = date;}

    public long getDate(){return date;}

    public void setUsage(long usage){this.usage = usage;}

    public long getUsage(){return usage;}

    public String getServer_id(){return server_id;}

    public void setServer_id(String server_id){this.server_id = server_id;}
}
