package shaishav.com.bebetter.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import shaishav.com.bebetter.data.contracts.PointContract;
import shaishav.com.bebetter.data.contracts.UsageContract;
import shaishav.com.bebetter.data.models.Point;
import shaishav.com.bebetter.data.providers.PointsProvider;
import shaishav.com.bebetter.data.providers.UsageProvider;

/**
 * Created by shaishavgandhi05 on 11/6/16.
 */

public class PointSource {

    public static void createPoint(Context mContext, ContentValues mContentValues) {
        mContext.getContentResolver().insert(PointsProvider.CONTENT_URI, mContentValues);
    }

    public static List<Point> getAllPoints(Context mContext) {
        Cursor cursor = mContext.getContentResolver().query(PointsProvider.CONTENT_URI, null, null, null,
                PointsProvider.QUERY_SORT_ORDER);

        List<Point> points = PointsProvider.cursorToListPoints(cursor);
        cursor.close();
        return points;
    }

    public static int getTotalPoints(Context mContext) {
        Cursor cursor = mContext.getContentResolver().query(PointsProvider.CONTENT_URI,
                new String[]{"SUM(" + PointContract.COLUMN_POINTS + ") "}, null, null, null);

        if(cursor.moveToFirst())
        {
            int total = cursor.getInt(0);
            cursor.close();
            return total;
        }

        if (cursor != null) {
            cursor.close();
        }
        return 0;
    }
}
