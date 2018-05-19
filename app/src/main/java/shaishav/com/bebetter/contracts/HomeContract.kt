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

package shaishav.com.bebetter.contracts

import shaishav.com.bebetter.data.models.*

/**
 * Created by shaishav.gandhi on 12/17/17.
 */
interface HomeContract {

  fun setAverageDaiyUsage(usage: Long)
  fun setDailyUsage(usage: Long)
  fun setTotalUsage(usage: Long)
  fun setGoals(goals: List<Goal>)
  fun setUsages(usages: List<Usage>)
  fun setCurrentGoal(goal: Goal)
  fun setCurrentStreak(currentStreak: Long)
  fun setTotalPoints(totalPoints: Long)
  fun setPoints(points: List<Point>)
  fun setLevel(level: Level)
  fun setSummary(summary: Summary)
  fun setTotalUnlocks(unlocks: Int)

}