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

import android.support.test.InstrumentationRegistry
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
import shaishav.com.bebetter.data.contracts.PointContract
import shaishav.com.bebetter.data.models.Point
import shaishav.com.bebetter.extensions.yesterday
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class) class PointsDatabaseManagerTest {

  lateinit var contentResolver: BriteContentResolver
  lateinit var briteDatabase: BriteDatabase
  lateinit var databaseManager: PointsDatabaseManager
  lateinit var databaseHelper: MySQLiteHelper

  companion object {
    @BeforeClass
    fun deleteData() {
      InstrumentationRegistry.getTargetContext().deleteDatabase(MySQLiteHelper.DATABASE_NAME)
    }
  }

  @Before
  @Throws fun setUp() {
    val sqlbrite = SqlBrite.Builder().build()
    contentResolver = sqlbrite.wrapContentProvider(InstrumentationRegistry.getTargetContext().contentResolver, Schedulers.io())
    databaseHelper = MySQLiteHelper(InstrumentationRegistry.getTargetContext())
    briteDatabase = sqlbrite.wrapDatabaseHelper(databaseHelper, Schedulers.io())

    databaseManager = PointsDatabaseManagerImpl(contentResolver, briteDatabase)
  }

  @Test fun totalPointsReturnsSum() {
    databaseHelper.writableDatabase.delete(PointContract.TABLE_POINTS, null, null)
    val point = Point(id = 0, date = Date().time, points = 54)
    databaseManager.savePoint(point).test()
    val point2 = Point(id = 0, date = Date().time, points = 66)
    databaseManager.savePoint(point2).test()

    val testObserver = databaseManager.totalPoints().test()

    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)

    assertEquals(54 + 66, testObserver.values()[0])
  }

  @Test fun savePointsSuccessfully() {
    databaseHelper.writableDatabase.delete(PointContract.TABLE_POINTS, null, null)
    val point = Point(id = 0, date = Date().time, points = 54)

    val testObserver = databaseManager.savePoint(point).test()

    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    testObserver.assertComplete()

    val reader = databaseManager.points().test()
    reader.awaitTerminalEvent(2, TimeUnit.SECONDS)
    reader.assertNoErrors()
    reader.assertValueCount(1)

    assertEquals(54, reader.values()[0][0].points)
  }

  @Test fun pointsOnYesterday() {
    databaseHelper.writableDatabase.delete(PointContract.TABLE_POINTS, null, null)

    val currentDate = Calendar.getInstance()
    currentDate.set(Calendar.DAY_OF_YEAR, currentDate.get(Calendar.DAY_OF_YEAR) - 1)
    val point = Point(id = 0, date = currentDate.timeInMillis, points = 54)

    databaseManager.savePoint(point).test()

    val testObserver = databaseManager.point(currentDate.timeInMillis).test()

    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    assertEquals(54, testObserver.values()[0].points)
  }

  @Test fun pointsWithDefaultData() {
    databaseHelper.writableDatabase.delete(PointContract.TABLE_POINTS, null, null)

    val testObserver = databaseManager.point(Date().time).test()

    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    assertEquals(0, testObserver.values()[0].points)
  }

  @Test fun averagePointsWithNoValues() {
    databaseHelper.writableDatabase.delete(PointContract.TABLE_POINTS, null, null)

    val testObserver = databaseManager.averagePoints().test()

    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    assertEquals(0, testObserver.values()[0])
  }

  @Test fun averagePointsWithAverage() {
    databaseHelper.writableDatabase.delete(PointContract.TABLE_POINTS, null, null)

    val currentDate = Calendar.getInstance()
    currentDate.set(Calendar.DAY_OF_YEAR, currentDate.get(Calendar.DAY_OF_YEAR) - 1)
    val point = Point(id = 0, date = currentDate.timeInMillis, points = 54)
    val point2 = Point(id = 1, date = currentDate.yesterday().timeInMillis, points = 46)

    databaseManager.savePoint(point).test()
    databaseManager.savePoint(point2).test()

    val testObserver = databaseManager.averagePoints().test()

    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    assertEquals(50, testObserver.values()[0])
  }

  @After @Throws fun cleanUp() {
    databaseHelper.writableDatabase.close()
  }
}