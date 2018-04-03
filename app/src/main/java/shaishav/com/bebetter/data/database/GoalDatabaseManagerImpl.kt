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

package shaishav.com.bebetter.data.database

import com.squareup.sqlbrite2.BriteContentResolver
import com.squareup.sqlbrite2.BriteDatabase
import io.reactivex.Completable
import io.reactivex.Observable
import shaishav.com.bebetter.data.contracts.GoalContract
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.data.providers.GoalProvider
import shaishav.com.bebetter.data.repository.GoalRepository
import shaishav.com.bebetter.di.scopes.ApplicationScope
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
@ApplicationScope class GoalDatabaseManagerImpl @Inject constructor(val contentResolver: BriteContentResolver, val database: BriteDatabase) : GoalDatabaseManager {

  override fun goals(): Observable<List<Goal>> {
    return contentResolver.createQuery(GoalProvider.CONTENT_URI, null, null, null,
            GoalProvider.QUERY_SORT_ORDER, false)
            .mapToList {
              return@mapToList GoalProvider.cursorToGoal(it)
            }
  }

  override fun goalOnDay(day: Long): Observable<Goal> {
    val currentDate = Calendar.getInstance()
    currentDate.timeInMillis = day
    currentDate.set(Calendar.HOUR_OF_DAY, 0)
    currentDate.set(Calendar.MINUTE, 0)
    val lower = currentDate.timeInMillis

    currentDate.set(Calendar.HOUR_OF_DAY, 23)
    currentDate.set(Calendar.MINUTE, 59)
    currentDate.set(Calendar.SECOND, 59)
    val higher = currentDate.timeInMillis
    return database.createQuery(GoalContract.TABLE_GOAL, "select * from ${GoalContract.TABLE_GOAL} where " +
            " ${GoalContract.COLUMN_DATE} > $lower AND ${GoalContract.COLUMN_DATE} < $higher")
            .mapToOne { return@mapToOne GoalProvider.cursorToGoal(it) }
  }

  override fun saveGoal(goal: Goal): Completable {
    return Completable.fromCallable {
      val currentDate = Calendar.getInstance()
      currentDate.timeInMillis = goal.date
      currentDate.set(Calendar.HOUR_OF_DAY, 0)
      currentDate.set(Calendar.MINUTE, 0)
      val lower = currentDate.timeInMillis

      currentDate.set(Calendar.HOUR_OF_DAY, 23)
      currentDate.set(Calendar.MINUTE, 59)
      currentDate.set(Calendar.SECOND, 59)
      val higher = currentDate.timeInMillis


      val cursor = database.query("select * from ${GoalContract.TABLE_GOAL} where " +
      " ${GoalContract.COLUMN_DATE} BETWEEN $lower AND $higher")
      if (cursor.count > 0) {
        throw Exception("Goal for current day already exists")
      }
      return@fromCallable database.insert(GoalContract.TABLE_GOAL, goal.toContentValues())
    }
  }
}