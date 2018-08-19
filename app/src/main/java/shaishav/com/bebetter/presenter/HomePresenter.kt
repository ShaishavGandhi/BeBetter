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
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.contracts.HomeContract
import shaishav.com.bebetter.data.repository.*
import shaishav.com.bebetter.extensions.yesterday
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/17/17.
 */
class HomePresenter @Inject constructor(
        var view: HomeContract?,
        val usageRepository: UsageRepository,
        val goalRepository: GoalRepository,
        private val streakRepository: StreakRepository,
        private val pointsRepository: PointsRepository,
        private val summaryRepository: SummaryRepository,
        private val lifecycleScopeProvider: LifecycleScopeProvider<*>) {

  init {
    refresh()
  }

  fun refresh() {
    dailyUsage()
    averageDailyUsage()
    currentStreak()
//    totalUnlocks()
    totalUsage()
    totalPoints()
    usageTrend()
    pointsTrend()
    currentGoal()
    level()
//    lastThreeDaysData()
  }

  fun lastThreeDaysData() {
    val calendar = Calendar.getInstance()
    summaryRepository.summary(calendar.timeInMillis)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(lifecycleScopeProvider)
            .subscribe({ summary ->
              view?.setSummary(summary)
            }, { error ->
              Timber.e(error)
            })
  }

  fun totalUnlocks() {
    usageRepository.totalUnlocks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(lifecycleScopeProvider)
            .subscribe({ unlocks ->
              view?.setTotalUnlocks(unlocks)
            }, {
              Timber.e(it)
            })
  }

  fun level() {
    pointsRepository
            .level()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(lifecycleScopeProvider)
            .subscribe({ level ->
              view?.setLevel(level)
            }, { error ->
              Timber.e(error)
            })
  }

  fun pointsTrend() {
    pointsRepository
            .points()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(lifecycleScopeProvider)
            .subscribe({ points ->
              view?.setPoints(points)
            }, { error ->
              Timber.e(error)
            })

  }

  fun totalPoints() {
    pointsRepository
            .totalPoints()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(lifecycleScopeProvider)
            .subscribe({ totalPoints ->
              view?.setTotalPoints(totalPoints)
            }, { error ->
              Timber.e(error)
            })
  }

  fun currentStreak() {
    streakRepository
            .currentStreak()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(lifecycleScopeProvider)
            .subscribe({ currentStreak ->
              view?.setCurrentStreak(currentStreak)
            }, { throwable ->
              Timber.e(throwable)
            })
  }

  fun currentGoal() {
    goalRepository
            .currentGoal()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(lifecycleScopeProvider)
            .subscribe({ goal ->
              view?.setCurrentGoal(goal)
            }, { error ->
              Timber.e(error)
            })
  }

  fun dailyUsage() {
    usageRepository
            .dailyUsage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(lifecycleScopeProvider)
            .subscribe({ dailyUsage ->
              view?.setDailyUsage(dailyUsage)
            }, { error ->
              Timber.e(error)
            })
  }

  fun averageDailyUsage() {
    usageRepository
            .averageDailyUsage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(lifecycleScopeProvider)
            .subscribe({
              view?.setAverageDaiyUsage(it)
            }, { error ->
              Timber.e(error)
            })
  }

  fun totalUsage() {
    usageRepository
            .totalUsage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(lifecycleScopeProvider)
            .subscribe({
              view?.setTotalUsage(it)
            }, { error ->
              Timber.e(error)
            })
  }

  fun usageTrend() {
    usages()
    goals()
  }

  fun usages() {
    usageRepository
            .usages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(lifecycleScopeProvider)
            .subscribe({
              view?.setUsages(it)
            }, { error ->
              Timber.e(error)
            })
  }

  fun goals() {
    goalRepository
            .goals()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(lifecycleScopeProvider)
            .subscribe({
              view?.setGoals(it)
            }, { error ->
              Timber.e(error)
            })
  }

  fun unsubscribe() {
    view = null
  }
}
