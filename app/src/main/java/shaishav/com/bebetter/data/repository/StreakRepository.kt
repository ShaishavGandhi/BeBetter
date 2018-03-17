package shaishav.com.bebetter.data.repository

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 3/17/18.
 */
class StreakRepository @Inject constructor(private val goalRepository: GoalRepository, private val usageRepository: UsageRepository) {

  fun currentStreak(): Observable<Long> {
    return Observable.combineLatest(goalRepository.goals(), usageRepository.usages(), BiFunction { goals, usages ->
      var currentStreak = 0L
      if (usages.isEmpty()) {
        return@BiFunction currentStreak
      }
      for (i in 0 until usages.size) {
        val usage = usages[i]
        val goal = goals[i]
        if (usage.usage < goal.goal) {
          currentStreak++
        } else {
          return@BiFunction currentStreak
        }
      }
      return@BiFunction currentStreak
    })
  }
}