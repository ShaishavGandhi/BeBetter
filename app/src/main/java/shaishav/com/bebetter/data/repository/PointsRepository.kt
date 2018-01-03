package shaishav.com.bebetter.data.repository

import io.reactivex.Observable
import shaishav.com.bebetter.data.database.PointsDatabaseManager
import shaishav.com.bebetter.data.models.Point
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
class PointsRepository @Inject constructor(private val databaseManager: PointsDatabaseManager) {

    fun points(): Observable<List<Point>> {
        return databaseManager.points()
    }
}