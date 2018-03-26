package shaishav.com.bebetter.data.database

import com.squareup.sqlbrite2.BriteContentResolver
import com.squareup.sqlbrite2.BriteDatabase
import io.reactivex.Completable
import io.reactivex.Observable
import shaishav.com.bebetter.data.contracts.PointContract
import shaishav.com.bebetter.data.models.Point
import shaishav.com.bebetter.data.providers.PointsProvider
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
class PointsDatabaseManagerImpl @Inject constructor(private val contentResolver: BriteContentResolver,
                                                    private val database: BriteDatabase) : PointsDatabaseManager {

  override fun points(): Observable<List<Point>> {
    return contentResolver.createQuery(PointsProvider.CONTENT_URI, null, null, null,
            PointsProvider.QUERY_SORT_ORDER, false)
            .mapToList {
              return@mapToList PointsProvider.cursorToPoints(it)
            }
  }

  override fun totalPoints(): Observable<Long> {
    return points().flatMap { points ->
      val sum = points.sumBy { it.points }
      return@flatMap Observable.just(sum.toLong())
    }
  }

  override fun savePoint(point: Point): Completable {
    val contentValues = point.toContentValues()
    return Completable.fromCallable {
      return@fromCallable database.insert(PointContract.TABLE_POINTS, contentValues)
    }
  }

}