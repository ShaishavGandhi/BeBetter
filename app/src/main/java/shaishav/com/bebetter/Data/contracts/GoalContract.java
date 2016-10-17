package shaishav.com.bebetter.Data.contracts;

/**
 * Created by shaishavgandhi05 on 10/16/16.
 */

public class GoalContract {

    public static final String TABLE_GOAL = "goal";

    public static final String COLUMN_GOAL = "goal";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_SERVER_ID = "server_id";

    public static final String CREATE_GOAL = "create table "
            + TABLE_GOAL + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_DATE
            + " long not null,"+COLUMN_GOAL+" long not null,"
            +COLUMN_SERVER_ID+" text default 'NA');";

}
