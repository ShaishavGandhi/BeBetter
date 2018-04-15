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

import android.content.ContentValues
import android.support.annotation.WorkerThread
import com.squareup.sqlbrite2.BriteContentResolver
import com.squareup.sqlbrite2.BriteDatabase
import io.reactivex.Observable
import shaishav.com.bebetter.data.contracts.UsageContract
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.data.providers.UsageProvider
import shaishav.com.bebetter.di.scopes.ApplicationScope
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
@ApplicationScope class UsageDatabaseManagerImpl @Inject constructor(
        val contentResolver: BriteContentResolver, val database: BriteDatabase
) : UsageDatabaseManager {

  override fun averageDailyUsage(): Observable<Long> {
    return contentResolver.createQuery(UsageProvider.CONTENT_URI,
            arrayOf("AVG(" + UsageContract.COLUMN_USAGE + ") "), null, null, null, false)
            .mapToOne {
              // TODO : Replace with preference
              return@mapToOne it.getInt(0).toLong() / (1000 * 60)
            }
  }

  override fun usages(): Observable<List<Usage>> {
    return contentResolver
            .createQuery(UsageProvider.CONTENT_URI,
                    null,
                    null,
                    null,
                    UsageProvider.QUERY_SORT_ORDER,
                    false)
            .mapToList {
              return@mapToList UsageProvider.cursorToUsage(it)
            }
  }

  override fun totalUsage(): Observable<Long> {
    return contentResolver.createQuery(UsageProvider.CONTENT_URI,
            arrayOf("SUM(" + UsageContract.COLUMN_USAGE + ") "), null, null, null, false)
            .mapToOne {
              // TODO : Replace with preference instead of static 1000 * 60
              return@mapToOne it.getInt(0).toLong() / (1000 * 60)
            }
  }

  @WorkerThread override fun insertSession(usage: Usage): Long {
    val contentValues = ContentValues()
    contentValues.put(UsageContract.COLUMN_DATE, usage.date)
    contentValues.put(UsageContract.COLUMN_USAGE, usage.usage)

    return database.insert(UsageContract.TABLE_USAGE, contentValues)
  }

  override fun usage(date: Long): Observable<Usage> {
    val currentDate = Calendar.getInstance()
    currentDate.timeInMillis = date
    currentDate.set(Calendar.HOUR_OF_DAY, 0)
    currentDate.set(Calendar.MINUTE, 0)
    val lower = currentDate.timeInMillis

    currentDate.set(Calendar.HOUR_OF_DAY, 23)
    currentDate.set(Calendar.MINUTE, 59)
    currentDate.set(Calendar.SECOND, 59)
    val higher = currentDate.timeInMillis

    return database.createQuery(UsageContract.TABLE_USAGE, "select * from ${UsageContract.TABLE_USAGE} where " +
            "${UsageContract.COLUMN_DATE} > $lower AND ${UsageContract.COLUMN_DATE} < $higher")
            .mapToOne { return@mapToOne UsageProvider.cursorToUsage(it) }
  }
}