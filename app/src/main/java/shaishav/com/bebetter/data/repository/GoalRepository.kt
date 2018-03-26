package shaishav.com.bebetter.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.data.database.GoalDatabaseManager
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.di.scopes.ApplicationScope
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
@ApplicationScope class GoalRepository @Inject constructor(private val databaseManager: GoalDatabaseManager) {

  fun goals(): Observable<List<Goal>> {
    return databaseManager.goals()
  }

  fun currentGoal(): Observable<Goal> {
    return databaseManager.goalOnDay(Date().time)
  }

  fun goal(time: Long): Observable<Goal> {
    return databaseManager.goalOnDay(time)
  }

  fun cloneGoal(previousDate: Long, date: Long): Completable {
    return goal(previousDate)
            .flatMapCompletable { currentGoal ->
              if (isSameDay(Date().time, currentGoal.date)) {
                return@flatMapCompletable Completable.error(Exception("Goal already exists for that day"))
              }
              val goalValue = currentGoal.goal
              val goal = Goal(0, date, goalValue)
              return@flatMapCompletable databaseManager.saveGoal(goal)
            }.subscribeOn(Schedulers.io())
  }

  fun saveGoal(goal: Goal): Completable {
    return databaseManager.saveGoal(goal)
  }

  fun isSameDay(day1: Long, day2: Long): Boolean {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = day1

    val calendar2 = Calendar.getInstance()
    calendar2.timeInMillis = day2

    return calendar.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
  }

}