package shaishav.com.bebetter.data.contracts

/**
 * Created by shaishav.gandhi on 3/18/18.
 */
object GoalContract {

  const val TABLE_GOAL = "goal"

  const val COLUMN_GOAL = "goal"
  const val COLUMN_ID = "_id"
  const val COLUMN_DATE = "date"
  const val COLUMN_SERVER_ID = "server_id"

  const val CREATE_GOAL = ("create table "
          + TABLE_GOAL + "(" + COLUMN_ID
          + " integer primary key autoincrement, " + COLUMN_DATE
          + " long not null," + COLUMN_GOAL + " long not null,"
          + COLUMN_SERVER_ID + " text default 'NA');")
}