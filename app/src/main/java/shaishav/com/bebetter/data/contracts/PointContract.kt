package shaishav.com.bebetter.data.contracts;

/**
 * Created by shaishavgandhi05 on 11/6/16.
 */

object PointContract {

    const val TABLE_POINTS = "points"

    const val COLUMN_POINTS = "points"
    const val COLUMN_ID = "_id"
    const val COLUMN_DATE = "date"

    const val CREATE_POINTS = ("create table "
    + TABLE_POINTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_DATE
            + " long not null,"+COLUMN_POINTS+" long not null);")

}
