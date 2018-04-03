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

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import shaishav.com.bebetter.data.models.Stat
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 2/26/18.
 */
class StatsRepository @Inject constructor(private val usageRepository: UsageRepository, private val goalRepository: GoalRepository) {

  /**
   * Method that gives the user's current daily usage
   * and their current goal. Useful for showing data in notification.
   *
   * @return Observable<Stat>
   */
  fun getStat(): Observable<Stat> {
    return Observable.combineLatest(usageRepository.dailyUsage(), goalRepository.currentGoal(), BiFunction { usage, goal ->
      return@BiFunction Stat(usage, goal.goal / (1000 * 60))
    })
  }

}