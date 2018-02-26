package shaishav.com.bebetter.data.preferences

import io.reactivex.Observable

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
interface PreferenceDataStore {

  /**
   * Get currently running session time.
   *
   * @param currentTime
   * @return Observable<Long>
   */
  fun currentSession(currentTime: Long): Observable<Long>

  /**
   * Get the current daily usage including the
   * current session time.
   *
   * @return Observable<Long>
   */
  fun dailyUsageSoFar(): Observable<Long>

  /**
   * Store phone lock time.
   * @param lockTime
   */
  fun insertPhoneLockTime(lockTime: Long)

  /**
   * Store the phone unlock time
   * @param unlockTime
   */
  fun insertPhoneUnlockTime(unlockTime: Long)

  /**
   * Get the last unlock time
   * @return The last unlock time (long)
   */
  fun lastUnlockTime(): Long

  /**
   * Get the last lock time
   *
   * @return Observable<Long>
   */
  fun lastLockTime(): Observable<Long>

  /**
   * Store the current day's usage
   *
   * @param sessionTime to be stored
   */
  fun storeCurrentDayUsage(sessionTime: Long)

  /**
   * Get the usage time for the current day.
   * Returns a Long instead of Observable. If you
   * want a stream of changes to the daily session,
   * then see @see dailyUsageSoFar()
   */
  fun rawDailyUsage(): Long
}