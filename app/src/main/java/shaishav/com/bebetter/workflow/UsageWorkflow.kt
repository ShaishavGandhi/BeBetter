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

package shaishav.com.bebetter.workflow

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.data.models.Point
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.data.preferences.PreferenceDataStore
import shaishav.com.bebetter.data.repository.GoalRepository
import shaishav.com.bebetter.data.repository.PointsRepository
import shaishav.com.bebetter.data.repository.StreakRepository
import shaishav.com.bebetter.data.repository.UsageRepository
import shaishav.com.bebetter.utils.NotificationHelper
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 1/10/18.
 */
class UsageWorkflow @Inject constructor(private val usageRepository: UsageRepository,
                                        private val goalRepository: GoalRepository,
                                        private val pointsRepository: PointsRepository,
                                        private val streakRepository: StreakRepository,
                                        private val notificationHelper: NotificationHelper) {

  /**
   * Workflow to go through when a phone is locked.
   * Stores the phone lock time and checks other conditions
   * to store session data
   *
   * @param lockTime
   */
  fun phoneLocked(lockTime: Long) {
    // Store phone locked time
    usageRepository.storePhoneLockedTime(lockTime)

    // Get the last unlocked time
    val unlockTime = usageRepository.lastUnlockedTime()

    when {
      unlockTime == 0L -> return
      hasDayChanged(lockTime, unlockTime) -> {
        // The day has passed between last lock and unlock

        // Copy over previous goal
        goalRepository.cloneGoal(unlockTime, lockTime)
                .subscribeOn(Schedulers.io())
                .subscribe({}, { _ -> })

        // Reset unlock counter
        usageRepository.resetUnlockCounter()

        // Construct the last minute of yesterday
        val previousDay = Calendar.getInstance()
        previousDay.timeInMillis = unlockTime
        previousDay.set(Calendar.HOUR_OF_DAY, 23)
        previousDay.set(Calendar.MINUTE, 59)

        // Get the last session time
        var currentSessionTime = previousDay.timeInMillis - unlockTime
        // Get the daily usage so far
        val dayUsage = usageRepository.rawDailyUsage()

        // Add it up to get final daily usage
        val usage = Usage(0, previousDay.timeInMillis, dayUsage + currentSessionTime)

        // Add points
        addPoints(previousDay.timeInMillis, usage)

        // Store in database
        usageRepository.insertSession(usage)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .subscribe({}, { _ -> })


        // Fast forward to the current date to calculate the session of the new day
        previousDay.set(Calendar.DAY_OF_YEAR, previousDay.get(Calendar.DAY_OF_YEAR) + 1)
        previousDay.set(Calendar.HOUR_OF_DAY, 0)
        previousDay.set(Calendar.MINUTE, 0)

        val lastUnlockTime = previousDay.timeInMillis

        // Calculate the new session
        currentSessionTime = lockTime - lastUnlockTime

        // Store phone unlock time
        usageRepository.storePhoneUnlockedTime(lastUnlockTime)
        // Store the session
        usageRepository.storeCurrentDayUsage(currentSessionTime)

        // Show notification to give them summary
        notificationHelper.createDailySummaryNotification()
      }
      else -> {
        // Still the same day. Just store the session time
        var sessionTime = lockTime - unlockTime
        // Get the daily usage so far and add to session
        val previousSession = usageRepository.rawDailyUsage()
        sessionTime += previousSession
        // Store in db
        usageRepository.storeCurrentDayUsage(sessionTime)
      }
    }

  }

  /**
   * Method to check whether the day has changed between the
   * last time phone was locked and unlocked
   *
   * @param lockTime phone lock time
   * @param unlockTime phone unlock time
   *
   * @return boolean
   */
  fun hasDayChanged(lockTime: Long, unlockTime: Long): Boolean {
    val lockDate = Calendar.getInstance()
    lockDate.timeInMillis = lockTime

    val unlockDate = Calendar.getInstance()
    unlockDate.timeInMillis = unlockTime

    return lockDate.get(Calendar.DAY_OF_MONTH) != unlockDate.get(Calendar.DAY_OF_MONTH)
  }

  /**
   * Workflow to go through when phone is unlocked.
   * Stores session timings and stores in database
   * if necessary
   *
   * @param unlockTime
   */
  fun phoneUnlocked(unlockTime: Long) {
    // Get the last unlocked time
    val lastUnlockedTime = usageRepository.lastUnlockedTime()

    if (hasDayChanged(lastUnlockedTime, unlockTime) && lastUnlockedTime != 0L) {
      // The day has changed. i.e User locked the phone before 12am, went
      // to sleep (presumably) and unlocked now. So we need to store
      // the previous session in database
      val currentSession = usageRepository.rawDailyUsage()
      val usage = Usage(0, lastUnlockedTime, currentSession)

      // Reset phone unlock count and increment
      usageRepository.resetUnlockCounter()
      usageRepository.incrementUnlockCounter()

      // Add points
      addPoints(lastUnlockedTime, usage)

      usageRepository.insertSession(usage)
              .subscribeOn(Schedulers.io())
              .subscribe({}, { _ -> })

      // Clone the goal
      goalRepository.cloneGoal(lastUnlockedTime, unlockTime)
              .subscribeOn(Schedulers.io())
              .subscribe({}, { _ -> })

      // Reset session data to zero
      usageRepository.storeCurrentDayUsage(0)

      // Show notification to give them summary
      notificationHelper.createDailySummaryNotification()
    }

    // Increment unlock counter
    usageRepository.incrementUnlockCounter()

    // Store the unlock time
    usageRepository.storePhoneUnlockedTime(unlockTime)
  }

  /**
   * Add points based on their usage, goal and streak.
   *
   * @param timeInMillis long representation of current day
   * @param usage Usage of the past day (since this is run after midnight)
   *
   */
  fun addPoints(timeInMillis: Long, usage: Usage) {
    val observable = Observable.combineLatest(goalRepository.goal(timeInMillis).firstElement().toObservable(),
            streakRepository.currentStreak(), BiFunction { goal: Goal, streak: Long ->
      // Default points
      var pointsAmt = 0
      var extraPoints = 0.0
      if (goal.goal > usage.usage) {
        // Usage is less than goal. Hooray! Give them 50 points
        pointsAmt += 50
        // These are extra points you get based on your goal
        // and streak. We want to reward people with lower
        // goals and more difference in their usage and goal
        extraPoints = (goal.goal - usage.usage).toDouble() / (goal.goal * goal.goal).toDouble()
        extraPoints *= Math.pow(10.0, 9.0)
      }

      // If they have a streak then reward them for that
      if (streak > 0) {
        extraPoints *= streak.toInt()
      }

      val point = Point(id = 0, date = timeInMillis, points = pointsAmt + Math.round(extraPoints).toInt())

      return@BiFunction point
    })

    observable.flatMapCompletable { point: Point ->
      return@flatMapCompletable pointsRepository.save(point)
    }.subscribeOn(Schedulers.io()).subscribe({
    }, { error ->
      Timber.e(error)
    })
  }

}