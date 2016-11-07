package shaishav.com.bebetter.data.contracts;

/**
 * Created by shaishavgandhi05 on 11/6/16.
 */

public class PointContract {

    public static final String TABLE_POINTS = "points";

    public static final String COLUMN_POINTS = "points";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";

    public static final String CREATE_POINTS = "create table "
            + TABLE_POINTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_DATE
            + " long not null,"+COLUMN_POINTS+" long not null);";

}
