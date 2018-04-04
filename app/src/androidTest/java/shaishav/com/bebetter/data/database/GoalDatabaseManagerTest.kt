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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import shaishav.com.bebetter.data.MySQLiteHelper
import shaishav.com.bebetter.data.contracts.GoalContract
import shaishav.com.bebetter.data.contracts.UsageContract
import shaishav.com.bebetter.data.models.Goal
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class) class GoalDatabaseManagerTest {

  lateinit var contentResolver: BriteContentResolver
  lateinit var briteDatabase: BriteDatabase
  lateinit var databaseManager: GoalDatabaseManager
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

    databaseManager = GoalDatabaseManagerImpl(contentResolver, briteDatabase)
  }

  @Test fun goalOnDayReturnsGoal() {
    databaseHelper.writableDatabase.delete(GoalContract.TABLE_GOAL, null, null)
    val calendar = Calendar.getInstance()
    val time = calendar.timeInMillis

    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1)
    val tomorrow = calendar.timeInMillis
    val goal = Goal(id = 0, date = time, goal = 180 * 1000 * 60)

    val secondGoal = Goal(id = 1, date = tomorrow, goal = 120 * 1000 * 60)
    databaseManager.saveGoal(goal).test()
    databaseManager.saveGoal(secondGoal).test()

    val testObserver = databaseManager.goalOnDay(time).test()
    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)
    assertEquals(180 * 1000 * 60, testObserver.values()[0].goal)
  }

  @Test fun goalOnDayReturnsSecondGoal() {
    databaseHelper.writableDatabase.delete(GoalContract.TABLE_GOAL, null, null)

    val calendar = Calendar.getInstance()
    val time = calendar.timeInMillis

    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1)
    val tomorrow = calendar.timeInMillis
    val goal = Goal(id = 0, date = time, goal = 180 * 1000 * 60)

    val secondGoal = Goal(id = 1, date = tomorrow, goal = 120 * 1000 * 60)
    databaseManager.saveGoal(goal).test()
    databaseManager.saveGoal(secondGoal).test()

    val testObserver = databaseManager.goalOnDay(tomorrow).test()
    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)
    assertEquals(120 * 1000 * 60, testObserver.values()[0].goal)
  }

  @Test fun saveGoalSuccessfully() {
    databaseHelper.writableDatabase.delete(GoalContract.TABLE_GOAL, null, null)

    val calendar = Calendar.getInstance()
    val time = calendar.timeInMillis

    val goal = Goal(id = 0, date = time, goal = 180 * 1000 * 60)

    val testObserver = databaseManager.saveGoal(goal).test()

    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    testObserver.assertComplete()
  }

  @Test fun saveGoalUnSuccessfully() {
    databaseHelper.writableDatabase.delete(GoalContract.TABLE_GOAL, null, null)

    val calendar = Calendar.getInstance()
    val time = calendar.timeInMillis

    val goal = Goal(id = 0, date = time, goal = 180 * 1000 * 60)

    val secondGoal = Goal(id = 1, date = time, goal = 120 * 1000 * 60)
    databaseManager.saveGoal(goal).test()
    val testObserver = databaseManager.saveGoal(secondGoal).test()

    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoValues()
    assertEquals(1, testObserver.errorCount())
  }

  @Test fun listOfGoals() {
    databaseHelper.writableDatabase.delete(GoalContract.TABLE_GOAL, null, null)

    val calendar = Calendar.getInstance()
    val time = calendar.timeInMillis

    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1)
    val tomorrow = calendar.timeInMillis
    val goal = Goal(id = 0, date = time, goal = 180 * 1000 * 60)

    val secondGoal = Goal(id = 1, date = tomorrow, goal = 120 * 1000 * 60)
    databaseManager.saveGoal(goal).test()
    databaseManager.saveGoal(secondGoal).test()

    val testObserver = databaseManager.goals().test()
    testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)
    assertEquals(2, testObserver.values()[0].size)
  }

}