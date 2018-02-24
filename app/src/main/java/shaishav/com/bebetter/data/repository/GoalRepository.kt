package shaishav.com.bebetter.data.repository

import io.reactivex.Observable
import shaishav.com.bebetter.data.database.GoalDatabaseManager
import shaishav.com.bebetter.data.models.Goal
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
class GoalRepository @Inject constructor(private val databaseManager: GoalDatabaseManager) {

  fun goals(): Observable<List<Goal>> {
    return databaseManager.goals()
  }

  fun currentGoal(): Observable<Goal> {
    return databaseManager.currentGoal()
  }
}