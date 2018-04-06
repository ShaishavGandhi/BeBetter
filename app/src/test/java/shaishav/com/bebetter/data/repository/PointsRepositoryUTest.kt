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
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import shaishav.com.bebetter.data.database.PointsDatabaseManager
import shaishav.com.bebetter.data.models.Level
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class) class PointsRepositoryUTest {

  @Mock lateinit var databaseManager: PointsDatabaseManager
  lateinit var repository: PointsRepository

  @Before @Throws fun setUp() {
    MockitoAnnotations.initMocks(this)
    repository = PointsRepository(databaseManager)
  }

  @Test fun testBeginnerLevel() {
    whenever(databaseManager.totalPoints()).thenReturn(Observable.just(0))

    val testObserver = repository.level().test()
    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)

    assertEquals(Level.BEGINNER, testObserver.values()[0])
  }

  @Test fun testBeginnerEdgeCase() {
    whenever(databaseManager.totalPoints()).thenReturn(Observable.just(999))

    val testObserver = repository.level().test()
    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)

    assertEquals(Level.BEGINNER, testObserver.values()[0])
  }

  @Test fun testApprenticeLevel() {
    whenever(databaseManager.totalPoints()).thenReturn(Observable.just(1000))

    val testObserver = repository.level().test()
    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)

    assertEquals(Level.APPRENTICE, testObserver.values()[0])
  }

}