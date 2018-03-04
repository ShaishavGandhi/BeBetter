package shaishav.com.bebetter.data.database

import io.reactivex.Observable
import io.reactivex.Single
import shaishav.com.bebetter.data.models.Usage

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
interface UsageDatabaseManager {

  /**
   * Returns the average daily usage of the
   * user over it's entire history. Returns
   * the formatted value and not the millisecond
   * value
   *
   * @return Observable<Long>
   */
  fun averageDailyUsage(): Observable<Long>

  /**
   * Returns a hot observable of the list
   * of all Usage in user's history.
   *
   * @return Observable<List<Usage>>
   */
  fun usages(): Observable<List<Usage>>

  /**
   * Returns the total usage of the user in
   * their entire history. Combines data from
   * historical usages, current daily usage and
   * current session.
   *
   * @return Observable<Long>
   */
  fun totalUsage(): Observable<Long>

  /**
   * Inserts a Usage object in database
   * given a Usage object.
   *
   * Synchronous method, should probably be converted
   * to a Completable TODO
   */
  fun insertSession(usage: Usage): Long
}