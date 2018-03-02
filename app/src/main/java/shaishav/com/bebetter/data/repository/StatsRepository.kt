package shaishav.com.bebetter.data.repository

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import shaishav.com.bebetter.data.models.Stat
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 2/26/18.
 */
class StatsRepository @Inject constructor(private val usageRepository: UsageRepository, private val goalRepository: GoalRepository) {

  fun getStat(): Observable<Stat> {
    return Observable.combineLatest(usageRepository.dailyUsage(), goalRepository.currentGoal(), BiFunction { usage, goal ->
      return@BiFunction Stat(usage, goal.goal / (1000 * 60))
    })
  }

}