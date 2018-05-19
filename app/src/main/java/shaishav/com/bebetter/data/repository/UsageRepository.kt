/*
 * Copyright (c) 2018 Shaishav Gandhi
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions
 *  and limitations under the License.
 */

package shaishav.com.bebetter.data.repository

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.data.database.UsageDatabaseManager
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.data.preferences.PreferenceDataStore
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/17/17.
 */
class UsageRepository @Inject constructor(val databaseManager: UsageDatabaseManager, val preferenceStore: PreferenceDataStore) {

  fun usages(): Observable<List<Usage>> {
    return databaseManager.usages().subscribeOn(Schedulers.io())
  }

  fun currentSession(): Observable<Long> {
    return preferenceStore.currentSession(Date().time).subscribeOn(Schedulers.io())
  }

  fun dailyUsage(): Observable<Long> {
    return preferenceStore.dailyUsageSoFar().subscribeOn(Schedulers.io())
  }

  fun rawDailyUsage(): Long {
    return preferenceStore.rawDailyUsage()
  }

  fun averageDailyUsage(): Observable<Long> {
    return databaseManager.averageDailyUsage().subscribeOn(Schedulers.io())
  }

  fun totalUsage(): Observable<Long> {
    return Observable.combineLatest(databaseManager.totalUsage(), preferenceStore.dailyUsageSoFar(), BiFunction { totalUsage, dailyUsageSoFar ->
      return@BiFunction totalUsage + dailyUsageSoFar
    })
  }

  fun insertSession(usage: Usage): Single<Long> {
    return Single.fromCallable {
      return@fromCallable databaseManager.insertSession(usage)
    }.subscribeOn(Schedulers.io())
  }

  fun storePhoneLockedTime(lockTime: Long) {
    preferenceStore.insertPhoneLockTime(lockTime)
  }

  fun storePhoneUnlockedTime(unlockTime: Long) {
    preferenceStore.insertPhoneUnlockTime(unlockTime)
  }

  fun lastUnlockedTime(): Long {
    return preferenceStore.lastUnlockTime()
  }

  fun storeCurrentDayUsage(sessionTime: Long) {
    preferenceStore.storeCurrentDayUsage(sessionTime)
  }

  fun usage(date: Long): Observable<Usage> {
    val currentDate = Calendar.getInstance()
    val givenDate = Calendar.getInstance()
    givenDate.timeInMillis = date

    if (currentDate.get(Calendar.DAY_OF_YEAR) == givenDate.get(Calendar.DAY_OF_YEAR)) {
      return dailyUsage().map { usageToday ->
        return@map Usage(id = 0, date = givenDate.timeInMillis, usage = usageToday * 1000 * 60)
      }
    }

    return databaseManager.usage(date).subscribeOn(Schedulers.io())
  }

  fun totalUnlocks(): Observable<Int> {
    return preferenceStore.totalUnlocks()
  }

  fun incrementUnlockCounter() {
    preferenceStore.incrementUnlockCounter()
  }

  fun resetUnlockCounter() {
    preferenceStore.resetUnlockCounter()
  }


}