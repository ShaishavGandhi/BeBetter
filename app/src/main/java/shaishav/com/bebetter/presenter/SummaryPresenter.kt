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

import com.uber.autodispose.LifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.contracts.SummaryContract
import shaishav.com.bebetter.data.repository.PointsRepository
import shaishav.com.bebetter.data.repository.StreakRepository
import shaishav.com.bebetter.data.repository.SummaryRepository
import shaishav.com.bebetter.data.repository.UsageRepository
import timber.log.Timber
import javax.inject.Inject

class SummaryPresenter @Inject constructor(
        var view: SummaryContract?,
        private val summaryRepository: SummaryRepository,
        private val usageRepository: UsageRepository,
        private val pointsRepository: PointsRepository,
        private val streakRepository: StreakRepository,
        private val lifecycleScopeProvider: LifecycleScopeProvider<*>
) {

  fun start(date: Long) {
    summaryRepository.summary(date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(lifecycleScopeProvider)
            .subscribe({ summary ->
              view?.setSummary(summary)
              if (summary.goal.goal > summary.usage.usage) {
                view?.setGoalAchieved()
              }
            }, { error ->
              Timber.e(error)
            })
    mostUsedApps(date)
  }

  fun averageUsage() {
    usageRepository.averageDailyUsage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(lifecycleScopeProvider)
            .subscribe({ averageUsage ->
              view?.setAverageUsage(averageUsage)
            }, { error ->
              Timber.e(error)
            })
  }

  fun averagePoints() {
    pointsRepository.averagePoints()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(lifecycleScopeProvider)
            .subscribe({ averagePoints ->
              view?.setAveragePoints(averagePoints)
            }, { error ->
              Timber.e(error)
            })
  }

  fun streak() {
    streakRepository.currentStreak()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(lifecycleScopeProvider)
            .subscribe({ streak ->
              view?.setStreak(streak)
            }, { error ->
              Timber.e(error)
            })
  }

  fun mostUsedApps(date: Long) {
    usageRepository
            .usageStats(date)
            .subscribeOn(Schedulers.io())
            .map { it.subList(0, Math.min(5, it.size)) }
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(lifecycleScopeProvider)
            .subscribe({
              view?.setMostUsedApps(it)
            }, {
              Timber.e(it)
            })
  }

  fun detach() {
    view = null
  }


}