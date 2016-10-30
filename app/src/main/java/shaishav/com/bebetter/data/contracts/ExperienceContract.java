package shaishav.com.bebetter.data.contracts;

/**
 * Created by shaishavgandhi05 on 10/16/16.
 */

public class ExperienceContract {

    public static final String TABLE_LESSON = "lessons";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_LESSON = "lesson";
    public static final String COLUMN_CATEGORY = "categories";
    public static final String COLUMN_CREATED_AT = "createdAt";
    public static final String COLUMN_SERVER_ID = "server_id";
    public static final String COLUMN_IS_PUBLIC="public";

    public static final String LESSON_CREATE_TABLE = "create table "
            + TABLE_LESSON + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TITLE
            + " text not null,"+COLUMN_LESSON+" text not null, "+COLUMN_CREATED_AT+" long not null, "
            +COLUMN_CATEGORY+" text, "+COLUMN_SERVER_ID+" text default 'NA', "+COLUMN_IS_PUBLIC+" int default 0);";

}
