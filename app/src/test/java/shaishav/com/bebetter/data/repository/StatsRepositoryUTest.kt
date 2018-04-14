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

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.functions.Predicate
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import shaishav.com.bebetter.data.models.Goal
import java.util.*

/**
 * Created by shaishav.gandhi on 3/4/18.
 */
@RunWith(JUnit4::class)
class StatsRepositoryUTest {

  lateinit var statsRepository: StatsRepository
  @Mock lateinit var goalRepository: GoalRepository
  @Mock lateinit var usageRepository: UsageRepository

  @Before @Throws fun setUp() {
    MockitoAnnotations.initMocks(this)
    statsRepository = StatsRepository(usageRepository, goalRepository)
  }

  @Test fun testGetStat() {
    val goal = Goal(goal = 200 * 1000 * 60, date = Date().time, id = 1)
    whenever(usageRepository.dailyUsage()).thenReturn(Observable.just(120))
    whenever(goalRepository.currentGoal()).thenReturn(Observable.just(goal))

    statsRepository
            .getStat()
            .test()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValueAt(0, { it.goal == goal.goal})
  }

}