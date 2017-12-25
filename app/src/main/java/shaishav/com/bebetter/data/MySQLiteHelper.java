package shaishav.com.bebetter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import shaishav.com.bebetter.data.contracts.GoalContract;
import shaishav.com.bebetter.data.contracts.PointContract;
import shaishav.com.bebetter.data.contracts.UsageContract;

public class MySQLiteHelper extends SQLiteOpenHelper {


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_LESSON = "lesson";
    public static final String COLUMN_CATEGORY = "categories";
    public static final String COLUMN_CREATED_AT = "createdAt";
    public static final String COLUMN_SERVER_ID = "server_id";
    public static final String COLUMN_IS_PUBLIC="public";


    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_USAGE="usage";

    public static final String COLUMN_GOAL = "goal";

    private static final String DATABASE_NAME = "bebetter.db";
    private static final int DATABASE_VERSION = 1;





    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(UsageContract.CREATE_USAGE_TABLE);
        database.execSQL(GoalContract.CREATE_GOAL);
        database.execSQL(PointContract.CREATE_POINTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}

