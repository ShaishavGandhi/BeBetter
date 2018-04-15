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

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.data.database.GoalDatabaseManager
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.di.scopes.ApplicationScope
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
@ApplicationScope class GoalRepository @Inject constructor(private val databaseManager: GoalDatabaseManager) {

  fun goals(): Observable<List<Goal>> {
    return databaseManager.goals().subscribeOn(Schedulers.io())
  }

  fun currentGoal(): Observable<Goal> {
    return databaseManager.goalOnDay(Date().time).subscribeOn(Schedulers.io())
  }

  fun goal(time: Long): Observable<Goal> {
    return databaseManager.goalOnDay(time).subscribeOn(Schedulers.io())
  }

  fun cloneGoal(previousDate: Long, date: Long): Completable {
    return goal(previousDate)
            .flatMapCompletable { currentGoal ->
              if (isSameDay(Date().time, currentGoal.date)) {
                return@flatMapCompletable Completable.error(Exception("Goal already exists for that day"))
              }
              val goalValue = currentGoal.goal
              val goal = Goal(0, date, goalValue)
              return@flatMapCompletable databaseManager.saveGoal(goal)
            }.subscribeOn(Schedulers.io())
  }

  fun saveGoal(goal: Goal): Completable {
    return databaseManager.saveGoal(goal).subscribeOn(Schedulers.io())
  }

  fun isSameDay(day1: Long, day2: Long): Boolean {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = day1

    val calendar2 = Calendar.getInstance()
    calendar2.timeInMillis = day2

    return calendar.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
  }

}