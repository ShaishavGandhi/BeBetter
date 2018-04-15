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

import io.reactivex.Observable
import io.reactivex.Single
import shaishav.com.bebetter.data.models.Usage

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
interface UsageDatabaseManager {

  /**
   * Returns the average daily usage of the
   * user over it's entire history. Returns
   * the formatted value and not the millisecond
   * value
   *
   * @return Observable<Long>
   */
  fun averageDailyUsage(): Observable<Long>

  /**
   * Returns a hot observable of the list
   * of all Usage in user's history from
   * latest -> earliest.
   *
   * @return Observable<List<Usage>>
   */
  fun usages(): Observable<List<Usage>>

  /**
   * Returns the total usage of the user in
   * their entire history. Combines data from
   * historical usages, current daily usage and
   * current session.
   *
   * @return Observable<Long>
   */
  fun totalUsage(): Observable<Long>

  /**
   * Inserts a Usage object in database
   * given a Usage object.
   *
   * Synchronous method, should probably be converted
   * to a Completable TODO
   */
  fun insertSession(usage: Usage): Long

  /**
   * Returns the usage of the user on
   * any given day
   *
   * @param date
   * @return Observable<Usage>
   */
  fun usage(date: Long): Observable<Usage>
}