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
        val streakRepository: StreakRepository,
        val pointsRepository: PointsRepository,
        val summaryRepository: SummaryRepository,
        val disposables: CompositeDisposable) {

  init {
    dailyUsage()
    averageDailyUsage()
    currentStreak()
    totalUnlocks()
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
    val disposable = summaryRepository.summary(calendar.timeInMillis)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ summary ->
              view?.setSummary(summary)
            }, { error ->
              Timber.e(error)
            })
    disposables.add(disposable)
  }

  fun totalUnlocks() {
    val disposable = usageRepository.totalUnlocks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ unlocks ->
              view?.setTotalUnlocks(unlocks)
            }, {
              Timber.e(it)
            })
  }

  fun level() {
    val disposable = pointsRepository
            .level()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({level ->
              view?.setLevel(level)
            }, { error ->
              Timber.e(error)
            })

    disposables.add(disposable)
  }

  fun pointsTrend() {
    val disposable = pointsRepository
            .points()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ points ->
              view?.setPoints(points)
            }, { error ->
              Timber.e(error)
            })

    disposables.add(disposable)
  }

  fun totalPoints() {
    val disposable = pointsRepository
            .totalPoints()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ totalPoints ->
              view?.setTotalPoints(totalPoints)
            }, { error ->
              Timber.e(error)
            })

    disposables.add(disposable)
  }

  fun currentStreak() {
    val disposable = streakRepository
            .currentStreak()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ currentStreak ->
              view?.setCurrentStreak(currentStreak)
            }, { throwable ->
              Timber.e(throwable)
            })
    disposables.add(disposable)
  }

  fun currentGoal() {
    val disposable = goalRepository
            .currentGoal()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ goal ->
              view?.setCurrentGoal(goal)
            }, { error ->
              Timber.e(error)
            })

    disposables.add(disposable)
  }

  fun dailyUsage() {
    val disposable = usageRepository
            .dailyUsage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ dailyUsage ->
              view?.setDailyUsage(dailyUsage)
            }, { error ->
              Timber.e(error)
            })

    disposables.add(disposable)
  }

  fun averageDailyUsage() {
    val disposable = usageRepository
            .averageDailyUsage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
              view?.setAverageDaiyUsage(it)
            }, { error ->
              Timber.e(error)
            })

    disposables.add(disposable)
  }

  fun totalUsage() {
    val disposable = usageRepository
            .totalUsage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
              view?.setTotalUsage(it)
            }, { error ->
              Timber.e(error)
            })

    disposables.add(disposable)
  }

  fun usageTrend() {
    usages()
    goals()
  }

  fun usages() {
    val disposable = usageRepository
            .usages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
              view?.setUsages(it)
            }, { error ->
              Timber.e(error)
            })

    disposables.add(disposable)
  }

  fun goals() {
    val disposable = goalRepository
            .goals()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
              view?.setGoals(it)
            }, { error ->
              Timber.e(error)
            })

    disposables.add(disposable)
  }

  fun unsubscribe() {
    disposables.dispose()
    view = null
  }
}