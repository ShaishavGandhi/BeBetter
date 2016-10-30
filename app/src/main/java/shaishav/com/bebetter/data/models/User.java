package shaishav.com.bebetter.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shaishav on 9/6/2016.
 */
public class User {

    @SerializedName("_id")
    String server_id;

    String name, email, gcm_id, photo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
