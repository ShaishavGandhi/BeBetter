package shaishav.com.bebetter.Data.contracts;

/**
 * Created by shaishavgandhi05 on 10/23/16.
 */

public class UsageContract {

    public static final String TABLE_USAGE = "usage";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_USAGE="usage";
    public static final String COLUMN_SERVER_ID = "server_id";

    public static final String CREATE_USAGE_TABLE = "create table "
            + TABLE_USAGE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_DATE
            + " long not null,"+COLUMN_USAGE+" long not null, "+COLUMN_SERVER_ID+" text default 'NA');";
}
