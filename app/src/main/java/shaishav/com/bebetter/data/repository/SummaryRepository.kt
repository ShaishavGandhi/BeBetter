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
import io.reactivex.functions.Function3
import shaishav.com.bebetter.data.models.Summary
import javax.inject.Inject

class SummaryRepository @Inject constructor(
        private val usageRepository: UsageRepository,
        private val goalRepository: GoalRepository,
        private val pointsRepository: PointsRepository
) {

  fun summary(date: Long): Observable<Summary> {
    return Observable.combineLatest(usageRepository.usage(date), goalRepository.goal(date),
            pointsRepository.point(date), Function3 { usage, goal, point ->
      return@Function3 Summary(usage, goal, point)
    })
  }


}