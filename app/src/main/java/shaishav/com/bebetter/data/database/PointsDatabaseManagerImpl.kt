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
import shaishav.com.bebetter.data.contracts.PointContract
import shaishav.com.bebetter.data.models.Point
import shaishav.com.bebetter.data.providers.PointsProvider
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
class PointsDatabaseManagerImpl @Inject constructor(private val contentResolver: BriteContentResolver,
                                                    private val database: BriteDatabase) : PointsDatabaseManager {

  override fun points(): Observable<List<Point>> {
    return contentResolver.createQuery(PointsProvider.CONTENT_URI, null, null, null,
            PointsProvider.QUERY_SORT_ORDER, false)
            .mapToList {
              return@mapToList PointsProvider.cursorToPoints(it)
            }
  }

  override fun totalPoints(): Observable<Long> {
    return points().flatMap { points ->
      val sum = points.sumBy { it.points }
      return@flatMap Observable.just(sum.toLong())
    }
  }

  override fun savePoint(point: Point): Completable {
    val contentValues = point.toContentValues()
    return Completable.fromCallable {
      return@fromCallable database.insert(PointContract.TABLE_POINTS, contentValues)
    }
  }

}