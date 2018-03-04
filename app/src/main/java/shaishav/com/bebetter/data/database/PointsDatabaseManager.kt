package shaishav.com.bebetter.data.database

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
}