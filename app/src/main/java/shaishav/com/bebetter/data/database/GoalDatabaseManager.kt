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
   * Returns the goal of a user on given date
   * The method will filter the goal table
   * by the current day and return the valid
   * goal
   *
   * @return Observable<Goal>
   */
  fun goalOnDay(day: Long): Observable<Goal>

  /**
   * Method to save a goal to database
   * given a Goal object. Will check if
   * a goal already exists for that particular
   * day and will throw exception if it does
   *
   * Completes or errors out
   */
  fun saveGoal(goal: Goal): Completable
}