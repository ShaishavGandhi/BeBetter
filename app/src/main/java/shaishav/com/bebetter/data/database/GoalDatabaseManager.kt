package shaishav.com.bebetter.data.database

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import shaishav.com.bebetter.data.models.Goal

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
interface GoalDatabaseManager {

  fun goals(): Observable<List<Goal>>
  fun currentGoal(): Observable<Goal>
  fun saveGoal(goal: Goal): Completable
}