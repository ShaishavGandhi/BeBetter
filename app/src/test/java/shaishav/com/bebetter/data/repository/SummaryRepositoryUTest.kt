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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.data.models.Point
import shaishav.com.bebetter.data.models.Usage
import java.util.*

class SummaryRepositoryUTest {

  lateinit var summaryRepository: SummaryRepository
  @Mock lateinit var usageRepository: UsageRepository
  @Mock lateinit var goalRepository: GoalRepository
  @Mock lateinit var pointsRepository: PointsRepository

  @Before @Throws fun setUp() {
    MockitoAnnotations.initMocks(this)
    summaryRepository = SummaryRepository(usageRepository, goalRepository, pointsRepository)
  }

  @Test fun testSummaryCleanCase() {
    val time = Calendar.getInstance().timeInMillis
    val usage = Usage(id = 0, date = time, usage = 39 * 1000 * 60)
    val goal = Goal(id = 0, date = time, goal = 120 * 1000 * 60)
    val point = Point(id = 0, date = time, points = 50)
    whenever(usageRepository.usage(time)).thenReturn(Observable.just(usage))
    whenever(goalRepository.goal(time)).thenReturn(Observable.just(goal))
    whenever(pointsRepository.point(time)).thenReturn(Observable.just(point))

    val testObserver = summaryRepository.summary(time).test()

    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)

    assertEquals(39 * 1000 * 60, testObserver.values()[0].usage.usage)
    assertEquals(120 * 1000 * 60, testObserver.values()[0].goal.goal)
    assertEquals(50, testObserver.values()[0].point.points)

  }
}