package shaishav.com.bebetter.data.preferences

import io.reactivex.Observable

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
interface PreferenceDataStore {

    fun currentSession(currentTime: Long): Observable<Long>
    fun dailyUsageSoFar(): Observable<Long>
}