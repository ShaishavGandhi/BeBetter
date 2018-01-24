package shaishav.com.bebetter.data.repository

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
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
        return Observable.combineLatest(databaseManager.totalUsage(), preferenceStore.dailyUsageSoFar(), BiFunction { totalUsage, dailyUsageSoFar ->
            return@BiFunction totalUsage + dailyUsageSoFar
        })
    }

    fun insertSession(usage: Usage): Single<Long> {
        return Single.just(databaseManager.insertSession(usage))
    }

    fun storePhoneLockedTime(lockTime: Long) {
        preferenceStore.insertPhoneLockTime(lockTime)
    }

    fun lastUnlockedTime(): Long {
        return preferenceStore.lastUnlockTime()
    }

    fun storeCurrentSession(sessionTime: Long) {
        preferenceStore.storeSessionTime(sessionTime)
    }


}