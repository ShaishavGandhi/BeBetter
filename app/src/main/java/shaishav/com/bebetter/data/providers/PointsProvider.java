/*
 * Copyright (c) 2018 Shaishav Gandhi
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions
 *  and limitations under the License.
 */

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
import androidx.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import shaishav.com.bebetter.data.MySQLiteHelper;
import shaishav.com.bebetter.data.contracts.PointContract;
import shaishav.com.bebetter.data.models.Point;
import shaishav.com.bebetter.utils.Constants;

/**
 * Created by shaishavgandhi05 on 11/6/16.
 */

public class PointsProvider extends ContentProvider {

    static final String PROVIDER_NAME = Constants.PACKAGE + "." + PointContract.TABLE_POINTS;
    static final String URL = "content://" + PROVIDER_NAME + "/" + PointContract.TABLE_POINTS;
    public static final Uri CONTENT_URI = Uri.parse(URL);

    static final int POINTS = 1;
    static final int POINT_ID = 2;

    private SQLiteDatabase db;
    private Map<String, String> POINTS_PROJECTION_MAP;

    public static final String QUERY_SORT_ORDER = PointContract.COLUMN_ID + " DESC";
    public static String QUERY_SELECTION_ARGS_USAGE_RANGE = PointContract.COLUMN_DATE + " > ? AND " + PointContract.COLUMN_DATE +
            " < ?";

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, PointContract.TABLE_POINTS, POINTS);
        uriMatcher.addURI(PROVIDER_NAME, PointContract.TABLE_POINTS + "/#", POINT_ID);
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
        qb.setTables(PointContract.TABLE_POINTS);

        switch (uriMatcher.match(uri)) {
            case POINTS:
                qb.setProjectionMap(POINTS_PROJECTION_MAP);
                break;

            case POINT_ID:
                qb.appendWhere( PointContract.COLUMN_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder == ""){
            sortOrder = PointContract.COLUMN_ID;
        }

        Cursor cursor = qb.query(db,	projection,	selection, selectionArgs,null, null, sortOrder);

        /**
         * register to watch a content URI for changes
         */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case POINTS:
                return "vnd.android.cursor.dir/vnd." + PROVIDER_NAME + "." + PointContract.TABLE_POINTS;

            case POINT_ID:
                return "vnd.android.cursor.item/vnd." + PROVIDER_NAME + "." + PointContract.TABLE_POINTS;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(PointContract.TABLE_POINTS, "", values);

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
            case POINTS:
                count = db.delete(PointContract.TABLE_POINTS, selection, selectionArgs);
                break;

            case POINT_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( PointContract.TABLE_POINTS, PointContract.COLUMN_ID +  " = " + id +
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
            case POINTS:
                count = db.update(PointContract.TABLE_POINTS, values, selection, selectionArgs);
                break;

            case POINT_ID:
                count = db.update(PointContract.TABLE_POINTS, values, PointContract.COLUMN_ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    public static Point cursorToPoints(Cursor cursor){
        long id = cursor.getLong(0);
        long date = cursor.getLong(cursor.getColumnIndex(PointContract.COLUMN_DATE));
        int points = cursor.getInt(cursor.getColumnIndex(PointContract.COLUMN_POINTS));

        return new Point(date, id, points);
    }

    public static List<Point> cursorToListPoints(Cursor cursor){
        List<Point> points = new ArrayList<>();
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            Point point = cursorToPoints(cursor);
            points.add(point);
            cursor.moveToNext();
        }

        return points;
    }
}
