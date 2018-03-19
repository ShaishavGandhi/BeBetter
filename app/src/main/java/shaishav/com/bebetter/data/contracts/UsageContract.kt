package shaishav.com.bebetter.data.contracts;

/**
 * Created by shaishavgandhi05 on 10/23/16.
 */

object UsageContract {

    const val TABLE_USAGE = "usage"

    const val COLUMN_ID = "_id"
    const val COLUMN_DATE = "date"
    const val COLUMN_USAGE="usage"
    const val COLUMN_SERVER_ID = "server_id"

    const val CREATE_USAGE_TABLE = ("create table "
            + TABLE_USAGE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_DATE
            + " long not null,"+COLUMN_USAGE+" long not null, "+COLUMN_SERVER_ID+" text default 'NA');")
}
