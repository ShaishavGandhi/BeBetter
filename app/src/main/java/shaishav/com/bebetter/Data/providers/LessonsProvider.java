package shaishav.com.bebetter.Data.providers;

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

import shaishav.com.bebetter.Data.MySQLiteHelper;
import shaishav.com.bebetter.Data.contracts.LessonContract;
import shaishav.com.bebetter.Data.models.Experience;
import shaishav.com.bebetter.Utils.Constants;

/**
 * Created by shaishavgandhi05 on 10/16/16.
 */

public class LessonsProvider extends ContentProvider {

    static final String PROVIDER_NAME = Constants.PACKAGE;
    static final String URL = "content://" + PROVIDER_NAME + "/" + LessonContract.TABLE_LESSON;
    public static final Uri CONTENT_URI = Uri.parse(URL);

    static final int LESSONS = 1;
    static final int LESSON_ID = 2;

    private SQLiteDatabase db;
    private Map<String, String> LESSONS_PROJECTION_MAP;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, LessonContract.TABLE_LESSON, LESSONS);
        uriMatcher.addURI(PROVIDER_NAME, LessonContract.TABLE_LESSON + "/#", LESSON_ID);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        MySQLiteHelper dbHelper = new MySQLiteHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(LessonContract.TABLE_LESSON);

        switch (uriMatcher.match(uri)) {
            case LESSONS:
                qb.setProjectionMap(LESSONS_PROJECTION_MAP);
                break;

            case LESSON_ID:
                qb.appendWhere( MySQLiteHelper.COLUMN_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder == ""){
            sortOrder = MySQLiteHelper.COLUMN_ID;
        }

        Cursor c = qb.query(db,	projection,	selection, selectionArgs,null, null, sortOrder);

        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case LESSONS:
                return "vnd.android.cursor.dir/vnd." + PROVIDER_NAME + "." + LessonContract.TABLE_LESSON;

            case LESSON_ID:
                return "vnd.android.cursor.item/vnd." + PROVIDER_NAME + "." + LessonContract.TABLE_LESSON;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long rowID = db.insert(LessonContract.TABLE_LESSON, "", contentValues);

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case LESSONS:
                count = db.delete(LessonContract.TABLE_LESSON, selection, selectionArgs);
                break;

            case LESSON_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( LessonContract.TABLE_LESSON, MySQLiteHelper.COLUMN_ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case LESSONS:
                count = db.update(LessonContract.TABLE_LESSON, contentValues, selection, selectionArgs);
                break;

            case LESSON_ID:
                count = db.update(LessonContract.TABLE_LESSON, contentValues, MySQLiteHelper.COLUMN_ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    public static Experience cursorToExperience(Cursor cursor){
        Experience post = new Experience();
        post.setId(cursor.getLong(0));
        post.setLesson(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_LESSON)));
        post.setCreated_at(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CREATED_AT)));
        post.setTitle(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_TITLE)));
        post.setCategory(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CATEGORY)));
        post.setServer_id(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SERVER_ID)));
        post.setIs_public(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_IS_PUBLIC)));
        return post;
    }

    public static List<Experience> cursorToExperienceList(Cursor cursor){
        List<Experience> experiences = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Experience post = cursorToExperience(cursor);
            experiences.add(post);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return experiences;
    }
}
