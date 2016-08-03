package shaishav.com.bebetter.Data;

/**
 * Created by Shaishav on 02-08-2016.
 */
public class Idea {

    long id, date;
    String title;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public void setDate(long date){
        this.date = date;
    }

    public long getDate(){
        return date;
    }

}
