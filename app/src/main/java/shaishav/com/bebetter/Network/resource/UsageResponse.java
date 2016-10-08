package shaishav.com.bebetter.Network.resource;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import shaishav.com.bebetter.Data.Models.Response;
import shaishav.com.bebetter.Data.Models.Usage;
import shaishav.com.bebetter.Data.Models.User;

/**
 * Created by Shaishav on 10/7/2016.
 */
public class UsageResponse extends Response {

    @SerializedName("response")
    List<Usage> usages;

    public List<Usage> getUsages() {
        return usages;
    }

    public void setUsages(List<Usage> usages) {
        this.usages = usages;
    }
}
