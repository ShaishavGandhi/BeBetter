package shaishav.com.bebetter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import shaishav.com.bebetter.data.contracts.GoalContract;
import shaishav.com.bebetter.data.contracts.PointContract;
import shaishav.com.bebetter.data.contracts.UsageContract;

public class MySQLiteHelper extends SQLiteOpenHelper {


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

