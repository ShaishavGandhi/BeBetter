package shaishav.com.bebetter.data.preferences

import io.reactivex.Observable

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
interface PreferenceDataStore {

    fun currentSession(currentTime: Long): Observable<Long>
    fun dailyUsageSoFar(): Observable<Long>

    fun insertPhoneLockTime(lockTime: Long)
    fun insertPhoneUnlockTime(unlcokTime: Long)

    fun lastUnlockTime(): Long
    fun lastLockTime(): Observable<Long>
    fun storeSessionTime(sessionTime: Long)
}