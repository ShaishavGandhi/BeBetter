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

package shaishav.com.bebetter.presenter

import com.nhaarman.mockito_kotlin.whenever
import com.uber.autodispose.TestLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.junit.Before
import org.junit.Ignore
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import shaishav.com.bebetter.contracts.HomeContract
import shaishav.com.bebetter.data.repository.*

/**
 * Created by shaishav.gandhi on 2/15/18.
 */
@RunWith(JUnit4::class)
@Ignore("Fix later")
class HomePresenterUTest {

  companion object {
    val currentSession = 1000 * 10 * 20L
    val dailyUsage = 1000 * 60 * 60L
    val averageDailyUsage = 1000 * 60 * 35L
  }

  @Mock lateinit var view: HomeContract
  @Mock lateinit var usageRepository: UsageRepository
  @Mock lateinit var goalRepository: GoalRepository
  @Mock lateinit var streakRepository: StreakRepository
  @Mock lateinit var pointsRepository: PointsRepository
  @Mock lateinit var summaryRepository: SummaryRepository
  lateinit var presenter: HomePresenter
  lateinit var scopeProvider: TestLifecycleScopeProvider

  @Before
  @Throws
  fun setUp() {
    setupData()
    scopeProvider = TestLifecycleScopeProvider.create()
    scopeProvider.start()
    presenter = HomePresenter(view, usageRepository, goalRepository, streakRepository, pointsRepository, summaryRepository, scopeProvider)
  }

  fun setupData() {
    whenever(usageRepository.currentSession()).thenReturn(Observable.just(currentSession))
    whenever(usageRepository.dailyUsage()).thenReturn(Observable.just(dailyUsage))
    whenever(usageRepository.averageDailyUsage()).thenReturn(Observable.just(averageDailyUsage))
  }

}