package shaishav.com.bebetter.data.database

import com.squareup.sqlbrite2.BriteContentResolver
import com.squareup.sqlbrite2.BriteDatabase
import io.reactivex.Completable
import io.reactivex.Observable
import shaishav.com.bebetter.data.contracts.GoalContract
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.data.providers.GoalProvider
import shaishav.com.bebetter.di.scopes.ApplicationScope
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
@ApplicationScope class GoalDatabaseManagerImpl @Inject constructor(val contentResolver: BriteContentResolver, val database: BriteDatabase) : GoalDatabaseManager {

  override fun goals(): Observable<List<Goal>> {
    return contentResolver.createQuery(GoalProvider.CONTENT_URI, null, null, null,
            GoalProvider.QUERY_SORT_ORDER, false)
            .mapToList {
              return@mapToList GoalProvider.cursorToGoal(it)
            }
  }

  override fun currentGoal(): Observable<Goal> {

    val calender = Calendar.getInstance()
    calender.set(Calendar.HOUR_OF_DAY, 23)
    calender.set(Calendar.MINUTE, 59)
    val high = calender.timeInMillis

    calender.set(Calendar.HOUR_OF_DAY, 0)
    calender.set(Calendar.MINUTE, 0)
    val low = calender.timeInMillis

    val whereClause = arrayOf(low.toString(), high.toString())
    return contentResolver
            .createQuery(GoalProvider.CONTENT_URI,
                    null,
                    GoalProvider.QUERY_SELECTION_ARGS_GOAL_RANGE,
                    whereClause,
                    GoalProvider.QUERY_SORT_ORDER,
                    false)
            .mapToOne {
              return@mapToOne GoalProvider.cursorToGoal(it)
            }
  }

  override fun saveGoal(goal: Goal): Completable {
    return Completable.fromCallable {
      return@fromCallable database.insert(GoalContract.TABLE_GOAL, goal.toContentValues())
    }
  }
}