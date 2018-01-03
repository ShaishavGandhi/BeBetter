package shaishav.com.bebetter.data.database

import com.squareup.sqlbrite3.BriteContentResolver
import io.reactivex.Observable
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.data.providers.GoalProvider
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
class GoalDatabaseManagerImpl @Inject constructor(val contentResolver: BriteContentResolver): GoalDatabaseManager {

    override fun goals(): Observable<List<Goal>> {
        return contentResolver.createQuery(GoalProvider.CONTENT_URI, null, null, null,
                GoalProvider.QUERY_SORT_ORDER, false)
                .mapToList {
                    return@mapToList GoalProvider.cursorToGoal(it)
                }
    }
}