package shaishav.com.bebetter.data.database

import io.reactivex.Completable
import io.reactivex.Observable
import shaishav.com.bebetter.data.models.Point

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
interface PointsDatabaseManager {


  /**
   * Returns a list of Point accrued
   * by the user. WIP
   *
   * @return Observable<List<Point>>
   */
  fun points(): Observable<List<Point>>

  /**
   * Returns the total points accrued
   * by the user
   *
   * @return Observable<Long>
   */
  fun totalPoints(): Observable<Long>

  /**
   * Save given point object in database
   *
   * @param point
   * @return Completable
   */
  fun savePoint(point: Point): Completable
}