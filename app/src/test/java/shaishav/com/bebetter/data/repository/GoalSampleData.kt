package shaishav.com.bebetter.data.repository

import org.mockito.Mockito
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.data.models.Usage
import java.util.*

/**
 * Created by shaishav.gandhi on 3/17/18.
 */
object GoalSampleData {

  fun getSampleGoals(count: Int): List<Goal> {
    val goals = ArrayList<Goal>()
    val calendar = Calendar.getInstance()
    for (i in 0 until count) {
      val goalTime = 200 * 1000 * 60L
      calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR))
      val date = calendar.timeInMillis

      val goal = Goal(id = 0, date = date, goal = goalTime)
      goals.add(goal)
    }
    return goals
  }
}