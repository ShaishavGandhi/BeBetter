package shaishav.com.bebetter.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.contracts.SummaryContract
import shaishav.com.bebetter.data.repository.GoalRepository
import shaishav.com.bebetter.data.repository.PointsRepository
import shaishav.com.bebetter.data.repository.StreakRepository
import shaishav.com.bebetter.data.repository.UsageRepository
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/17/17.
 */
class SummaryPresenter @Inject constructor(
        var view: SummaryContract?,
        val usageRepository: UsageRepository,
        val goalRepository: GoalRepository,
        val streakRepository: StreakRepository,
        val pointsRepository: PointsRepository,
        val disposables: CompositeDisposable) {

  init {
    currentSession()
    dailyUsage()
    averageDailyUsage()
    currentStreak()
    totalUsage()
    totalPoints()
    usageTrend()
    currentGoal()
  }


  fun currentSession() {
    val disposable = usageRepository
            .currentSession()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
              view?.setCurrentSession(it)
            }
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
              error.printStackTrace()
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

            })
    disposables.add(disposable)
  }

  fun currentGoal() {
    val disposable = goalRepository
            .currentGoal()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { goal ->
              view?.setCurrentGoal(goal)
            }

    disposables.add(disposable)
  }

  fun dailyUsage() {
    val disposable = usageRepository
            .dailyUsage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
              view?.setDailyUsage(it)
            }

    disposables.add(disposable)
  }

  fun averageDailyUsage() {
    val disposable = usageRepository
            .averageDailyUsage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
              view?.setAverageDaiyUsage(it)
            }

    disposables.add(disposable)
  }

  fun totalUsage() {
    val disposable = usageRepository
            .totalUsage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
              view?.setTotalUsage(it)
            }

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
            .subscribe {
              view?.setUsages(it)
            }

    disposables.add(disposable)
  }

  fun goals() {
    val disposable = goalRepository
            .goals()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
              view?.setGoals(it)
            }

    disposables.add(disposable)
  }

  fun unsubscribe() {
    disposables.dispose()
    view = null
  }
}