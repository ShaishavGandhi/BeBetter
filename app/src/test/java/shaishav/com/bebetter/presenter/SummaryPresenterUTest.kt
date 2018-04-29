/*
 *  Copyright (c) 2018 Shaishav Gandhi
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
 *
 */

package shaishav.com.bebetter.presenter

import com.nhaarman.mockito_kotlin.timeout
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import shaishav.com.bebetter.contracts.SummaryContract
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.data.models.Point
import shaishav.com.bebetter.data.models.Summary
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.data.repository.PointsRepository
import shaishav.com.bebetter.data.repository.StreakRepository
import shaishav.com.bebetter.data.repository.SummaryRepository
import shaishav.com.bebetter.data.repository.UsageRepository
import shaishav.com.bebetter.extensions.yesterday
import java.util.*

@RunWith(JUnit4::class)
class SummaryPresenterUTest {

  @Mock lateinit var view: SummaryContract
  lateinit var presenter: SummaryPresenter
  @Mock lateinit var summaryRepository: SummaryRepository
  @Mock lateinit var pointsRepository: PointsRepository
  @Mock lateinit var usageRepository: UsageRepository
  @Mock lateinit var streakRepository: StreakRepository

  @Before @Throws fun setUp() {
    RxAndroidPlugins.setInitMainThreadSchedulerHandler({ Schedulers.trampoline() })
    MockitoAnnotations.initMocks(this)
    presenter = SummaryPresenter(view, summaryRepository, usageRepository, pointsRepository, streakRepository,
            CompositeDisposable())
  }

  @Test fun testStartReturns() {
    val time = Calendar.getInstance().yesterday().timeInMillis

    val goal = Goal(id = 0, goal = 120 * 60 * 1000, date = time)
    val points = Point(id = 0, points = 120, date = time)
    val usage = Usage(id = 0, usage = 110 * 60 * 1000, date = time)
    val summary = Summary(usage = usage, goal = goal, point = points)
    whenever(summaryRepository.summary(time)).thenReturn(Observable.just(summary))

    presenter.start(time)

    verify(view, timeout(100)).setSummary(summary)
    verify(view, timeout(100)).setGoalAchieved()
  }

  @Test fun testStartDoesntHitGoal() {
    val time = Calendar.getInstance().yesterday().timeInMillis

    val goal = Goal(id = 0, goal = 120 * 60 * 1000, date = time)
    val points = Point(id = 0, points = 120, date = time)
    val usage = Usage(id = 0, usage = 130 * 60 * 1000, date = time)
    val summary = Summary(usage = usage, goal = goal, point = points)
    whenever(summaryRepository.summary(time)).thenReturn(Observable.just(summary))

    presenter.start(time)

    verify(view, timeout(100)).setSummary(summary)
  }

  @Test fun testAverageUsage() {
    val usage = 100 * 1000 * 60L
    whenever(usageRepository.averageDailyUsage()).thenReturn(Observable.just(usage))

    presenter.averageUsage()

    verify(view, timeout(100)).setAverageUsage(usage)
  }

  @Test fun testAveragePoints() {
    val points = 54
    whenever(pointsRepository.averagePoints()).thenReturn(Observable.just(points))

    presenter.averagePoints()

    verify(view, timeout(100)).setAveragePoints(points)
  }

  @Test fun testStreak() {
    val streak = 1L
    whenever(streakRepository.currentStreak()).thenReturn(Observable.just(streak))

    presenter.streak()

    verify(view, timeout(100)).setStreak(streak)
  }
}