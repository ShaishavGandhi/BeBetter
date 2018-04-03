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
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 3/17/18.
 */
class StreakRepository @Inject constructor(private val goalRepository: GoalRepository, private val usageRepository: UsageRepository) {

  fun currentStreak(): Observable<Long> {
    return Observable.combineLatest(goalRepository.goals(), usageRepository.usages(), BiFunction { goals, usages ->
      var currentStreak = 0L
      if (usages.isEmpty()) {
        return@BiFunction currentStreak
      }
      for (i in 0 until usages.size) {
        val usage = usages[i]
        val goal = goals[i]
        if (usage.usage < goal.goal) {
          currentStreak++
        } else {
          return@BiFunction currentStreak
        }
      }
      return@BiFunction currentStreak
    })
  }
}