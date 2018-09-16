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
import android.content.pm.PackageManager
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
        private val packageManager: PackageManager,
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
   * @param time the time since epoch of the day you want usage of. Defaults to current day.
   * @return usage for current day.
   */
  fun rawDailyUsage(time: Long = System.currentTimeMillis()): Long {
    return rawUsageStats(time)
            .map { entry ->
              Timber.d("${entry.packageName} with time ${entry.usage.toFormattedTime()}")
              entry.usage
            }.sum()
  }

  /**
   * Get usage stats for the given [time].
   *
   * We go through all the Events that are recorded by the phone and
   * look for foreground and background events. These are important since
   * the delta actually tells us the amount of time it's been used.
   * We construct a map and log the usage and add to it to build a picture of
   * the day.
   *
   * @param time the time since epoch of the day you want usage of. Defaults to current day.
   * @return usage for current day.
   */
  private fun rawUsageStats(time: Long): List<UsageStat> {
    // Map of installed packages.
    val installedPackages = packageManager.getInstalledApplications(0)
            .map { it.packageName to it }
            .toMap()

    // Set lower boundary. I.e 12am of the given date.
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)

    Timber.d("Querying with ${calendar.timeInMillis} and $time")
    // Get all usage events.
    val events = usageStatsManager.queryEvents(calendar.timeInMillis, System.currentTimeMillis())

    val usageStatMap = HashMap<String, UsageStat>()
    var start = 0L
    while (events.hasNextEvent()) {
      // Load the event
      val event = UsageEvents.Event()
      events.getNextEvent(event)

      // Skip for the launcher
      if (event.timeStamp <= calendar.timeInMillis || event.packageName.contains("launcher")) {
        continue
      }

      // App has moved to foreground. Record timestamp.
      if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
        start = event.timeStamp
      }

      // App has moved to background.
      if (event.eventType == UsageEvents.Event.MOVE_TO_BACKGROUND) {
        // Let's calculate delta.
        val time = event.timeStamp - start

        val appInfo = installedPackages[event.packageName]
        appInfo?.let {
          // Get app name
          val appName = packageManager.getApplicationLabel(appInfo)
          // Get app icon
          val icon = packageManager.getApplicationIcon(appInfo)
          // Construct usage stat.
          val usageStat = usageStatMap.getOrElse(event.packageName) { UsageStat(event.packageName, appName, icon, 0) }
          // Increment time
          usageStat.usage = usageStat.usage + time
          usageStatMap[event.packageName] = usageStat
        }
      }
    }
    return usageStatMap
            .values
            .sortedByDescending { it.usage }
  }

  fun usageStats(time: Long): Observable<List<UsageStat>> {
    return Observable.fromCallable {
      return@fromCallable rawUsageStats(time)
    }
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