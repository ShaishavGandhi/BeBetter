package shaishav.com.bebetter.data.database

import com.squareup.sqlbrite2.BriteContentResolver
import io.reactivex.Observable
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.data.providers.GoalProvider
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
class GoalDatabaseManagerImpl @Inject constructor(val contentResolver: BriteContentResolver) : GoalDatabaseManager {

  override fun goals(): Observable<List<Goal>> {
    return contentResolver.createQuery(GoalProvider.CONTENT_URI, null, null, null,
            GoalProvider.QUERY_SORT_ORDER, false)
            .mapToList {
              return@mapToList GoalProvider.cursorToGoal(it)
            }
  }

  override fun currentGoal(): Observable<Goal> {

    val calender = Calendar.getInstance()
    calender.set(Calendar.HOUR, 23)
    calender.set(Calendar.MINUTE, 59)
    val high = calender.timeInMillis

    calender.set(Calendar.HOUR, 0)
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
}