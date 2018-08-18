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

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.data.database.UsageDatabaseManager
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.data.models.UsageStat
import shaishav.com.bebetter.data.preferences.PreferenceDataStore
import shaishav.com.bebetter.extensions.toFormattedTime
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/17/17.
 */
class UsageRepository @Inject constructor(
        private val databaseManager: UsageDatabaseManager,
        private val usageStatsManager: UsageStatsManager,
        private val preferenceStore: PreferenceDataStore) {

  fun usages(): Observable<List<Usage>> {
    return databaseManager.usages().subscribeOn(Schedulers.io())
  }

  fun currentSession(): Observable<Long> {
    return preferenceStore.currentSession(Date().time).subscribeOn(Schedulers.io())
  }

  fun dailyUsage(): Observable<Long> {
    return Observable.fromCallable {
      return@fromCallable rawDailyUsage()
    }
  }

  /**
   * Gets usage for the current day.
   *
   * We go through all the Events that are recorded by the phone and
   * look for foreground and background events. These are important since
   * the delta actually tells us the amount of time it's been used.
   * We construct a map and log the usage and add to it to build a picture of
   * the day.
   *
   * @return usage for current day.
   */
  fun rawDailyUsage(): Long {
    val calendar = Calendar.getInstance()
    val currentTime = calendar.timeInMillis
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)

    Timber.d("Querying with ${calendar.timeInMillis} and $currentTime")
    val events = usageStatsManager.queryEvents(calendar.timeInMillis, System.currentTimeMillis())
    val map = HashMap<String, UsageStat>()

    var start = 0L
    while (events.hasNextEvent()) {
      val event = UsageEvents.Event()
      events.getNextEvent(event)
      if (event.timeStamp <= calendar.timeInMillis || event.packageName.contains("launcher")) {
        continue
      }
      if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
        start = event.timeStamp
      }
      if (event.eventType == UsageEvents.Event.MOVE_TO_BACKGROUND) {
        val time = event.timeStamp - start
        val usageStat = map.getOrElse(event.packageName) { UsageStat(event.packageName, 0) }
        usageStat.usage = usageStat.usage + time
        map[event.packageName] = usageStat
      }
    }
    return map.values
            .sortedByDescending { it.usage }
            .map { entry ->
              Timber.d("${entry.packageName} with time ${entry.usage.toFormattedTime()}")
              entry.usage
            }.sum()
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
        return@map Usage(id = 0, date = givenDate.timeInMillis, usage = usageToday)
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