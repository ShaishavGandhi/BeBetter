package shaishav.com.bebetter.data.database

import com.squareup.sqlbrite3.BriteContentResolver
import io.reactivex.Observable
import shaishav.com.bebetter.data.models.Point
import shaishav.com.bebetter.data.providers.PointsProvider

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
class PointsDatabaseManagerImpl(val contentResolver: BriteContentResolver): PointsDatabaseManager {

    override fun points(): Observable<List<Point>> {
        return contentResolver.createQuery(PointsProvider.CONTENT_URI, null, null, null,
                PointsProvider.QUERY_SORT_ORDER, false)
                .mapToList {
                    return@mapToList PointsProvider.cursorToPoints(it)
                }
    }
}