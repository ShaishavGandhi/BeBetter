package shaishav.com.bebetter.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shaishav on 19-06-2016.
 */
public class Constants {

    public static final String PREFERENCES = "com.bebetter.com";
    public static final String FULL_NAME = "first_name";
    public static final String LOCKED = "locked";
    public static final String UNLOCKED = "unlocked";
    public static final String SESSION = "session";
    public static final String EMAIL = "email";
    public static final String DISPLAY_PIC="display_pic";

    public static String getFormattedDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("MMM dd");
        return dateFormat.format(date);
    }
}
