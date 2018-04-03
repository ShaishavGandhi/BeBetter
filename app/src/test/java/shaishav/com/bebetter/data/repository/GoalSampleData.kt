/*
 * Copyright (c) 2018 Shaishav Gandhi
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions
 *  and limitations under the License.
 */

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