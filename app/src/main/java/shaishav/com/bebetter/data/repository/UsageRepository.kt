package shaishav.com.bebetter.data.repository

import io.reactivex.Observable
import shaishav.com.bebetter.data.database.UsageDatabaseManager
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.data.preferences.PreferenceDataStore
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/17/17.
 */
class UsageRepository @Inject constructor(val databaseManager: UsageDatabaseManager, val preferenceStore: PreferenceDataStore) {

    fun usages() : Observable<List<Usage>> {
        return databaseManager.usages()
    }


    fun currentSession(): Observable<Long> {
        return preferenceStore.currentSession(Date().time)
    }

    fun dailyUsage(): Observable<Long> {
        return preferenceStore.dailyUsageSoFar()
    }

    fun averageDailyUsage(): Observable<Long> {
        return databaseManager.averageDailyUsage()
    }

    fun totalUsage(): Observable<Long> {
        return databaseManager.totalUsage()
    }


}