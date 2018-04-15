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

import android.database.sqlite.SQLiteDatabaseLockedException
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import shaishav.com.bebetter.data.database.UsageDatabaseManager
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.data.preferences.PreferenceDataStore
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
@RunWith(JUnit4::class)
class UsageRepositoryUTest {

    @Mock lateinit var databaseManager: UsageDatabaseManager
    @Mock lateinit var preferenceStore: PreferenceDataStore
    lateinit var repository: UsageRepository

    @Before @Throws fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = UsageRepository(databaseManager, preferenceStore)
    }

    @Test fun testUsages_returnsList() {
        val testObserver = TestObserver<List<Usage>>()

        val usages = UsageSampleData.getSampleUsages(5)
        whenever(databaseManager.usages()).thenReturn(Observable.just(usages))

        repository.usages().subscribe(testObserver)

        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        // No errors
        testObserver.assertNoErrors()
        // 1 value
        testObserver.assertValueCount(1)
        // Assert value count
        assertEquals(usages.size, testObserver.values()[0].size)

    }

    @Test fun testUsages_returnsError() {
        val testObserver = TestObserver<List<Usage>>()

        whenever(databaseManager.usages()).thenReturn(Observable.error(SQLiteDatabaseLockedException()))

        repository.usages().subscribe(testObserver)

        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        // No values
        testObserver.assertNoValues()
        // Assert value count
        assertEquals(1, testObserver.errorCount())
    }

    @Test fun testCurrentSession_returnsSession() {
        val testObserver = TestObserver<Long>()

        val currentTime = Date().time
        val sampleSession = Date().time - 1000 * 60 * 5
        whenever(preferenceStore.currentSession(any())).thenReturn(Observable.just(currentTime - sampleSession))

        repository.currentSession().subscribe(testObserver)

        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        // No errors
        testObserver.assertNoErrors()
        // Assert value count
        testObserver.assertValueCount(1)
    }

    @Test fun testDailyUsage_returnsUsage() {
        val testObserver = TestObserver<Long>()

        val sampleSession = 1000 * 60 * 75L
        whenever(preferenceStore.dailyUsageSoFar()).thenReturn(Observable.just(sampleSession))

        repository.dailyUsage().subscribe(testObserver)

        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        // No errors
        testObserver.assertNoErrors()
        // Assert value count
        testObserver.assertValueCount(1)
        // Assert value
        assertEquals(sampleSession, testObserver.values()[0])
    }

    @Test fun testAverageDailyUsage_returnsAverageUsage() {
        val testObserver = TestObserver<Long>()

        val sampleSession = 1000 * 60 * 75L
        whenever(databaseManager.averageDailyUsage()).thenReturn(Observable.just(sampleSession))

        repository.averageDailyUsage().subscribe(testObserver)

        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        // No errors
        testObserver.assertNoErrors()
        // Assert value count
        testObserver.assertValueCount(1)
        // Assert value
        assertEquals(sampleSession, testObserver.values()[0])
    }

    @Test fun testTotalUsage_returnsTotalUsage() {
        val testObserver = TestObserver<Long>()

        val totalUsage = 1000 * 60 * 60 * 4L
        val dailyUsage = 1000 * 60 * 60L
        whenever(databaseManager.totalUsage()).thenReturn(Observable.just(totalUsage))
        whenever(preferenceStore.dailyUsageSoFar()).thenReturn(Observable.just(dailyUsage))

        repository.totalUsage().subscribe(testObserver)
        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        // No errors
        testObserver.assertNoErrors()
        // 1 value
        testObserver.assertValueCount(1)
        // Value assertion
        assertEquals(totalUsage + dailyUsage, testObserver.values()[0])
    }
}