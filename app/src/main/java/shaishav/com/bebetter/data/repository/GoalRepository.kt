package shaishav.com.bebetter.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.data.database.GoalDatabaseManager
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.di.scopes.ApplicationScope
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
@ApplicationScope class GoalRepository @Inject constructor(private val databaseManager: GoalDatabaseManager) {

  fun goals(): Observable<List<Goal>> {
    return databaseManager.goals()
  }

  fun currentGoal(): Observable<Goal> {
    return databaseManager.currentGoal()
  }

  fun cloneGoal(date: Long): Completable {
    return currentGoal()
            .flatMapCompletable { currentGoal ->
              // TODO: Check if goal is of same day. Otherwise don't save it
              val goalValue = currentGoal.goal
              val goal = Goal(0, date, goalValue)
              return@flatMapCompletable databaseManager.saveGoal(goal)
            }.subscribeOn(Schedulers.io())
  }

  fun saveGoal(goal: Goal): Completable {
    return databaseManager.saveGoal(goal)
  }

}