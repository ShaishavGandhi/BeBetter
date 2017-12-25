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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import shaishav.com.bebetter.data.MySQLiteHelper;
import shaishav.com.bebetter.data.contracts.GoalContract;
import shaishav.com.bebetter.data.contracts.UsageContract;
import shaishav.com.bebetter.data.models.Usage;
import shaishav.com.bebetter.utils.Constants;

/**
 * Created by shaishavgandhi05 on 10/23/16.
 */

public class UsageProvider extends ContentProvider{

    static final String PROVIDER_NAME = Constants.PACKAGE + "." + UsageContract.TABLE_USAGE;
    static final String URL = "content://" + PROVIDER_NAME + "/" + UsageContract.TABLE_USAGE;
    public static final Uri CONTENT_URI = Uri.parse(URL);

    static final int USAGES = 1;
    static final int USAGE_ID = 2;

    private SQLiteDatabase db;
    private Map<String, String> USAGE_PROJECTION_MAP;

    public static final String QUERY_SORT_ORDER = UsageContract.COLUMN_ID + " DESC";
    public static String QUERY_SELECTION_ARGS_USAGE_RANGE = GoalContract.COLUMN_DATE + " > ? AND " + GoalContract.COLUMN_DATE +
            " < ?";

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, UsageContract.TABLE_USAGE, USAGES);
        uriMatcher.addURI(PROVIDER_NAME, UsageContract.TABLE_USAGE + "/#", USAGE_ID);
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
        qb.setTables(UsageContract.TABLE_USAGE);

        switch (uriMatcher.match(uri)) {
            case USAGES:
                qb.setProjectionMap(USAGE_PROJECTION_MAP);
                break;

            case USAGE_ID:
                qb.appendWhere( UsageContract.COLUMN_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder == ""){
            sortOrder = UsageContract.COLUMN_ID;
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
            case USAGES:
                return "vnd.android.cursor.dir/vnd." + PROVIDER_NAME + "." + UsageContract.TABLE_USAGE;

            case USAGE_ID:
                return "vnd.android.cursor.item/vnd." + PROVIDER_NAME + "." + UsageContract.TABLE_USAGE;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(UsageContract.TABLE_USAGE, "", values);

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
            case USAGES:
                count = db.delete(UsageContract.TABLE_USAGE, selection, selectionArgs);
                break;

            case USAGE_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( UsageContract.TABLE_USAGE, UsageContract.COLUMN_ID +  " = " + id +
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

        switch (uriMatcher.match(uri)){
            case USAGES:
                count = db.update(UsageContract.TABLE_USAGE, values, selection, selectionArgs);
                break;

            case USAGE_ID:
                count = db.update(UsageContract.TABLE_USAGE, values, UsageContract.COLUMN_ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    public static Usage cursorToUsage(Cursor cursor){
        Usage post = new Usage();
        post.setId(cursor.getLong(0));
        post.setDate(cursor.getLong(cursor.getColumnIndex(UsageContract.COLUMN_DATE)));
        post.setUsage(cursor.getLong(cursor.getColumnIndex(UsageContract.COLUMN_USAGE)));
        post.setServer_id(cursor.getString(cursor.getColumnIndex(UsageContract.COLUMN_SERVER_ID)));

        return post;
    }

    public static List<Usage> cursorToListUsage(Cursor cursor){
        List<Usage> usages = new ArrayList<>();
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            Usage usage = cursorToUsage(cursor);
            usages.add(usage);
            cursor.moveToNext();
        }

        return usages;
    }
}
