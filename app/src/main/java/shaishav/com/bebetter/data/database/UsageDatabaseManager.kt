package shaishav.com.bebetter.data.database

import io.reactivex.Observable
import io.reactivex.Single
import shaishav.com.bebetter.data.models.Usage

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
interface UsageDatabaseManager {

    fun averageDailyUsage(): Observable<Long>
    fun usages(): Observable<List<Usage>>
    fun totalUsage(): Observable<Long>
    fun insertSession(usage: Usage): Long
}