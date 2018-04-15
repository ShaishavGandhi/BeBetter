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

import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.runner.AndroidJUnit4
import com.squareup.sqlbrite2.BriteContentResolver
import com.squareup.sqlbrite2.BriteDatabase
import com.squareup.sqlbrite2.SqlBrite
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import shaishav.com.bebetter.data.MySQLiteHelper
import shaishav.com.bebetter.data.contracts.UsageContract
import shaishav.com.bebetter.data.models.Usage
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class) class UsageDatabaseManagerTest {

  lateinit var contentResolver: BriteContentResolver
  lateinit var briteDatabase: BriteDatabase
  lateinit var databaseManager: UsageDatabaseManager
  lateinit var databaseHelper: MySQLiteHelper

  companion object {
    @BeforeClass fun deleteData() {
      getTargetContext().deleteDatabase(MySQLiteHelper.DATABASE_NAME)
    }
  }

  @Before @Throws fun setUp() {
    val sqlbrite = SqlBrite.Builder().build()
    contentResolver = sqlbrite.wrapContentProvider(getTargetContext().contentResolver, Schedulers.io())
    databaseHelper = MySQLiteHelper(getTargetContext())
    briteDatabase = sqlbrite.wrapDatabaseHelper(databaseHelper, Schedulers.io())

    databaseManager = UsageDatabaseManagerImpl(contentResolver, briteDatabase)
  }

  @Test fun averageDailyUsageReturnsAverage() {
    val usage = Usage(id = 0, date = Date().time, usage =  100 * 1000 * 60L)
    val usage2 = Usage(id = 0, date = Date().time, usage =  50 * 1000 * 60L)
    databaseManager.insertSession(usage)
    databaseManager.insertSession(usage2)

    val testObserver = databaseManager.averageDailyUsage().test()
    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)

    assertEquals(75, testObserver.values()[0])
  }

  @Test fun averageDailyUsageReturnsZero() {
    databaseHelper.writableDatabase.delete(UsageContract.TABLE_USAGE, null, null)
    val testObserver = databaseManager.averageDailyUsage().test()
    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)

    assertEquals(0, testObserver.values()[0])
  }

  @Test fun totalUsageReturnsZero() {
    databaseHelper.writableDatabase.delete(UsageContract.TABLE_USAGE, null, null)
    val testObserver = databaseManager.totalUsage().test()
    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)

    assertEquals(0, testObserver.values()[0])
  }

  @Test fun totalUsageReturnsTotalUsage() {
    databaseHelper.writableDatabase.delete(UsageContract.TABLE_USAGE, null, null)
    val usage = Usage(id = 0, date = Date().time, usage =  100 * 1000 * 60L)
    val usage2 = Usage(id = 0, date = Date().time, usage =  50 * 1000 * 60L)
    databaseManager.insertSession(usage)
    databaseManager.insertSession(usage2)

    val testObserver = databaseManager.totalUsage().test()
    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)

    assertEquals(150, testObserver.values()[0])
  }

  @Test fun insertSessionSuccessfully() {
    databaseHelper.writableDatabase.delete(UsageContract.TABLE_USAGE, null, null)

    val usage = Usage(id = 0, date = Date().time, usage = 100 * 1000 * 60)
    databaseManager.insertSession(usage)

    val testObserver = databaseManager.usages().test()
    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)
    assertEquals(1, testObserver.values()[0].size)
  }

  @Test fun usageYesterdayReturnsSuccessfully() {
    databaseHelper.writableDatabase.delete(UsageContract.TABLE_USAGE, null, null)
    val currentDate = Calendar.getInstance()
    currentDate.set(Calendar.DAY_OF_YEAR, currentDate.get(Calendar.DAY_OF_YEAR) - 1)

    val usage = Usage(id = 0, date = currentDate.timeInMillis, usage = 100 * 1000 * 60)
    databaseManager.insertSession(usage)
    currentDate.set(Calendar.DAY_OF_YEAR, currentDate.get(Calendar.DAY_OF_YEAR) - 1)
    val usage2 = Usage(id = 0, date = currentDate.timeInMillis, usage = 120 * 1000 * 60)
    databaseManager.insertSession(usage2)

    val testObserver = databaseManager.usage(currentDate.timeInMillis).test()
    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)
    assertEquals(120 * 1000 * 60, testObserver.values()[0].usage)
  }

  @After @Throws fun tearDown() {
    databaseHelper.writableDatabase.close()
  }

}