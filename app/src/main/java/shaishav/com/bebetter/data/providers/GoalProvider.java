package shaishav.com.bebetter.data.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import shaishav.com.bebetter.data.MySQLiteHelper;
import shaishav.com.bebetter.data.contracts.GoalContract;
import shaishav.com.bebetter.data.models.Goal;
import shaishav.com.bebetter.utils.Constants;

/**
 * Created by shaishavgandhi05 on 10/16/16.
 */

public class GoalProvider extends ContentProvider {

  static final String PROVIDER_NAME = Constants.PACKAGE + "." + GoalContract.TABLE_GOAL;
  static final String URL = "content://" + PROVIDER_NAME + "/" + GoalContract.TABLE_GOAL;
  public static final Uri CONTENT_URI = Uri.parse(URL);

  public static String QUERY_SORT_ORDER = GoalContract.COLUMN_DATE + " ASC";
  public static String QUERY_SELECTION_ARGS_GOAL_RANGE = GoalContract.COLUMN_DATE + " > ? AND " + GoalContract.COLUMN_GOAL +
          " < ?";

  static final int GOALS = 1;
  static final int GOAL_ID = 2;

  private SQLiteDatabase db;
  private Map<String, String> GOALS_PROJECTION_MAP;

  static final UriMatcher uriMatcher;

  static {
    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    uriMatcher.addURI(PROVIDER_NAME, GoalContract.TABLE_GOAL, GOALS);
    uriMatcher.addURI(PROVIDER_NAME, GoalContract.TABLE_GOAL + "/#", GOAL_ID);
  }


  @Override
  public boolean onCreate() {
    Context context = getContext();
    MySQLiteHelper dbHelper = new MySQLiteHelper(context);
    db = dbHelper.getWritableDatabase();
    return (db == null) ? false : true;
  }

  @Nullable
  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(GoalContract.TABLE_GOAL);

    switch (uriMatcher.match(uri)) {
      case GOALS:
        qb.setProjectionMap(GOALS_PROJECTION_MAP);
        break;

      case GOAL_ID:
        qb.appendWhere(GoalContract.COLUMN_ID + "=" + uri.getPathSegments().get(1));
        break;

      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    if (sortOrder == null || sortOrder == "") {
      sortOrder = GoalContract.COLUMN_ID;
    }

    Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

    /**
     * register to watch a content URI for changes
     */
    c.setNotificationUri(getContext().getContentResolver(), uri);
    return c;
  }

  @Nullable
  @Override
  public String getType(Uri uri) {
    switch (uriMatcher.match(uri)) {
      case GOALS:
        return "vnd.android.cursor.dir/vnd." + PROVIDER_NAME + "." + GoalContract.TABLE_GOAL;

      case GOAL_ID:
        return "vnd.android.cursor.item/vnd." + PROVIDER_NAME + "." + GoalContract.TABLE_GOAL;

      default:
        throw new IllegalArgumentException("Unsupported URI: " + uri);
    }
  }

  @Nullable
  @Override
  public Uri insert(Uri uri, ContentValues values) {
    long rowID = db.insert(GoalContract.TABLE_GOAL, "", values);

    if (rowID > 0) {
      Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
      getContext().getContentResolver().notifyChange(_uri, null);
      return _uri;
    }
    throw new SQLException("Failed to add a record into " + uri);
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int count = 0;

    switch (uriMatcher.match(uri)) {
      case GOALS:
        count = db.delete(GoalContract.TABLE_GOAL, selection, selectionArgs);
        break;

      case GOAL_ID:
        String id = uri.getPathSegments().get(1);
        count = db.delete(GoalContract.TABLE_GOAL, GoalContract.COLUMN_ID + " = " + id +
                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
        break;

      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    getContext().getContentResolver().notifyChange(uri, null);
    return count;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    int count = 0;

    switch (uriMatcher.match(uri)) {
      case GOALS:
        count = db.update(GoalContract.TABLE_GOAL, values, selection, selectionArgs);
        break;

      case GOAL_ID:
        count = db.update(GoalContract.TABLE_GOAL, values, GoalContract.COLUMN_ID +
                " = " + uri.getPathSegments().get(1) +
                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
        break;

      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return count;
  }

  public static Goal cursorToGoal(Cursor cursor) {
    long id = cursor.getLong(0);
    long date = cursor.getLong(cursor.getColumnIndex(GoalContract.COLUMN_DATE));
    long goalValue = cursor.getLong(cursor.getColumnIndex(GoalContract.COLUMN_GOAL));
    return new Goal(id, date, goalValue);
  }

  public static List<Goal> cursorToListGoals(Cursor cursor) {
    List<Goal> goals = new ArrayList<>();
    cursor.moveToFirst();

    while (!cursor.isAfterLast()) {
      Goal goal = cursorToGoal(cursor);
      goals.add(goal);
      cursor.moveToNext();
    }

    cursor.close();
    return goals;
  }
}
