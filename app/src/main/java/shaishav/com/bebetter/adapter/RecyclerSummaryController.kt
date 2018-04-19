/*
 *  Copyright (c) 2018 Shaishav Gandhi
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
 *
 */

package shaishav.com.bebetter.adapter

import com.airbnb.epoxy.EpoxyController
import shaishav.com.bebetter.data.models.Summary
import shaishav.com.bebetter.models.GoalAchievedModel_
import shaishav.com.bebetter.models.SummaryModel_

class RecyclerSummaryController: EpoxyController() {

  var summary: Summary? = null
  set(value) {
    field = value
    requestModelBuild()
  }

  var goalAchieved = false
  set(value) {
    field = value
    requestModelBuild()
  }

  override fun buildModels() {
    addGoalAchievedModel()
    addSummaryModel()
  }

  private fun addGoalAchievedModel() {
    GoalAchievedModel_()
            .id("achieved_$goalAchieved")
            .addIf(goalAchieved, this)
  }

  private fun addSummaryModel() {
    summary?.let { summary ->
      SummaryModel_(summary)
              .id("${summary.goal.goal}_${summary.point.points}_${summary.usage.usage}")
              .addTo(this)
    }
  }
}