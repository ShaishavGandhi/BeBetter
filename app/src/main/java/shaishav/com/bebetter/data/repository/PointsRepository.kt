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
import shaishav.com.bebetter.data.database.PointsDatabaseManager
import shaishav.com.bebetter.data.models.Point
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
class PointsRepository @Inject constructor(private val databaseManager: PointsDatabaseManager) {

  fun points(): Observable<List<Point>> {
    return databaseManager.points()
  }

  fun totalPoints(): Observable<Long> {
    return databaseManager.totalPoints()
  }

  fun save(point: Point): Completable {
    return databaseManager.savePoint(point)
  }

}