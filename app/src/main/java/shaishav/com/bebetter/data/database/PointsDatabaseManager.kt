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

import io.reactivex.Completable
import io.reactivex.Observable
import shaishav.com.bebetter.data.models.Point

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
interface PointsDatabaseManager {


  /**
   * Returns a list of Point accrued
   * by the user. WIP
   *
   * @return Observable<List<Point>>
   */
  fun points(): Observable<List<Point>>

  /**
   * Returns the total points accrued
   * by the user
   *
   * @return Observable<Long>
   */
  fun totalPoints(): Observable<Long>

  /**
   * Save given point object in database
   *
   * @param point
   * @return Completable
   */
  fun savePoint(point: Point): Completable

  /**
   * Returns points earned by user
   * on given date
   *
   * @param date
   * @return Observable<Point>
   */
  fun point(date: Long): Observable<Point>

  fun averagePoints(): Observable<Int>
}