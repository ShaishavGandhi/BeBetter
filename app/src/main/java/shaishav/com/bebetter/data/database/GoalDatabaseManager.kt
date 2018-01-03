package shaishav.com.bebetter.data.database

import io.reactivex.Observable
import shaishav.com.bebetter.data.models.Goal

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
interface GoalDatabaseManager {

    fun goals(): Observable<List<Goal>>
}