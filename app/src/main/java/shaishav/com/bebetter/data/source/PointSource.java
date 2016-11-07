package shaishav.com.bebetter.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import shaishav.com.bebetter.data.models.Point;
import shaishav.com.bebetter.data.providers.PointsProvider;

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
}
