package shaishav.com.bebetter.Data;

/**
 * Created by Shaishav on 15-03-2016.
 */
public class Lesson {

    String title,lesson,category;
    long id,server_id,created_at;

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

    public long getServer_id(){
        return server_id;
    }

    public void setServer_id(long server_id){
        this.server_id = server_id;
    }

    public long getCreated_at(){
        return created_at;
    }

    public void setCreated_at(long created_at){
        this.created_at = created_at;
    }

}
