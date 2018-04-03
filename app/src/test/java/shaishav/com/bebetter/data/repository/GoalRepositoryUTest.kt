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

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.timeout
import org.mockito.MockitoAnnotations
import shaishav.com.bebetter.data.database.GoalDatabaseManager
import shaishav.com.bebetter.data.models.Goal
import java.util.*

/**
 * Created by shaishav.gandhi on 3/5/18.
 */
@RunWith(JUnit4::class)
class GoalRepositoryUTest {

  lateinit var goalRepository: GoalRepository
  @Mock lateinit var databaseManager: GoalDatabaseManager

  @Before @Throws fun setUp() {
    MockitoAnnotations.initMocks(this)
    goalRepository = GoalRepository(databaseManager)
  }

  @Ignore @Test fun testCloneGoal() {
    val goal = Goal(id = 0, date = Date().time,goal = 220 * 1000 * 60)
    val time = Date().time
    whenever(databaseManager.goalOnDay(time)).thenReturn(Observable.just(goal))
    whenever(databaseManager.saveGoal(any())).thenReturn(Completable.complete())

    goalRepository.cloneGoal(time, Date().time)
            .test()
            .assertNoErrors()

    verify(databaseManager, timeout(200)).saveGoal(any())
  }

  @Test fun testIsSameDay_sameDay_returnsTrue() {
    val day1 = Calendar.getInstance()
    val day2 = Calendar.getInstance()
    day2.set(Calendar.HOUR_OF_DAY, day2.get(Calendar.HOUR_OF_DAY) + 1)

    assertTrue(goalRepository.isSameDay(day1.timeInMillis, day2.timeInMillis))
  }

  @Test fun testIsSameDay_differentDay_returnsTrue() {
    val day1 = Calendar.getInstance()
    val day2 = Calendar.getInstance()
    day2.set(Calendar.DAY_OF_YEAR, day2.get(Calendar.DAY_OF_YEAR) + 1)

    assertFalse(goalRepository.isSameDay(day1.timeInMillis, day2.timeInMillis))
  }

}