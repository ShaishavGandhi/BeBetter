package shaishav.com.bebetter.Data.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shaishav on 15-03-2016.
 */
public class Experience {

    String title,lesson,category;

    @SerializedName("categories")
    String[] category_array;

    @SerializedName("_id")
    String server_id;
    long id,created_at;
    @SerializedName("public")
    boolean isPublicBool;
    int is_public;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getLesson(){
        return lesson;
    }

    public void setLesson(String lesson){
        this.lesson = lesson;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getServer_id(){
        return server_id;
    }

    public void setServer_id(String server_id){
        this.server_id = server_id;
    }

    public long getCreated_at(){
        return created_at;
    }

    public void setCreated_at(long created_at){
        this.created_at = created_at;
    }

    public void setIs_public(int is_public){
        this.is_public = is_public;
    }

    public int getIs_public(){
        return is_public;
    }

}
