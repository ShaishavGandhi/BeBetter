package shaishav.com.bebetter.data.database

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import shaishav.com.bebetter.data.models.Goal

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
interface GoalDatabaseManager {

  /**
   * Returns a hot observable of the user's
   * entire Goal history
   *
   * @return Observable<List<Goal>>
   */
  fun goals(): Observable<List<Goal>>

  /**
   * Returns the current goal of a user.
   * The method will filter the goal table
   * by the current day and return the valid
   * goal
   *
   * @return Observable<Goal>
   */
  fun currentGoal(): Observable<Goal>

  /**
   * Method to save a goal to database
   * given a Goal object.
   *
   * Completes or errors out
   */
  fun saveGoal(goal: Goal): Completable
}