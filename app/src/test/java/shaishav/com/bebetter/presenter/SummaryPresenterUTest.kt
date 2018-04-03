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
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.junit.Before
import org.junit.Ignore
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import shaishav.com.bebetter.contracts.SummaryContract
import shaishav.com.bebetter.data.repository.GoalRepository
import shaishav.com.bebetter.data.repository.PointsRepository
import shaishav.com.bebetter.data.repository.StreakRepository
import shaishav.com.bebetter.data.repository.UsageRepository

/**
 * Created by shaishav.gandhi on 2/15/18.
 */
@RunWith(JUnit4::class)
@Ignore("Fix later")
class SummaryPresenterUTest {

  companion object {
    val currentSession = 1000 * 10 * 20L
    val dailyUsage = 1000 * 60 * 60L
    val averageDailyUsage = 1000 * 60 * 35L
  }

  @Mock lateinit var view: SummaryContract
  @Mock lateinit var usageRepository: UsageRepository
  @Mock lateinit var goalRepository: GoalRepository
  @Mock lateinit var streakRepository: StreakRepository
  @Mock lateinit var pointsRepository: PointsRepository
  lateinit var presenter: SummaryPresenter

  @Before
  @Throws
  fun setUp() {
    setupData()
    presenter = SummaryPresenter(view, usageRepository, goalRepository, streakRepository, pointsRepository, CompositeDisposable())
  }

  fun setupData() {
    whenever(usageRepository.currentSession()).thenReturn(Observable.just(currentSession))
    whenever(usageRepository.dailyUsage()).thenReturn(Observable.just(dailyUsage))
    whenever(usageRepository.averageDailyUsage()).thenReturn(Observable.just(averageDailyUsage))
  }

}