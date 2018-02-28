package shaishav.com.bebetter.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import shaishav.com.bebetter.data.contracts.GoalContract
import shaishav.com.bebetter.data.contracts.PointContract
import shaishav.com.bebetter.data.contracts.UsageContract

/**
 * Created by shaishav.gandhi on 2/27/18.
 */
class MySQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

  override fun onCreate(database: SQLiteDatabase) {
    database.execSQL(UsageContract.CREATE_USAGE_TABLE)
    database.execSQL(GoalContract.CREATE_GOAL)
    database.execSQL(PointContract.CREATE_POINTS)
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

  }

  companion object {


    private val DATABASE_NAME = "bebetter.db"
    private val DATABASE_VERSION = 1
  }

}