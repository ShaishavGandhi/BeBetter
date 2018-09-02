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
import shaishav.com.bebetter.R
import shaishav.com.bebetter.data.models.Summary
import shaishav.com.bebetter.data.models.UsageStat
import shaishav.com.bebetter.models.*
import shaishav.com.bebetter.utils.ResourceManager

class RecyclerSummaryController(private val resourceManager: ResourceManager): EpoxyController() {

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

  var averageUsage: Long = 0
  set(value) {
    field = value
    requestModelBuild()
  }

  var averagePoints: Int = -1
  set(value) {
    field = value
    requestModelBuild()
  }

  var streak: Long = -1
  set(value) {
    field = value
    requestModelBuild()
  }

  var mostUsedApps: List<UsageStat> = emptyList()
  set(value) {
    field = value
    requestModelBuild()
  }

  override fun buildModels() {
    addGoalAchievedModel()
    addSummaryModel()
    addAverageUsageModel()
    addMostUsedApps()
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

  private fun addAverageUsageModel() {
    if (averageUsage > 0) {
      summary?.let { summary ->
        val usage = summary.usage
        AverageUsageModel_(summary, averageUsage, averagePoints, streak, resourceManager)
                .id("${averageUsage}_${usage.usage}_${averagePoints}_$streak")
                .addTo(this)
      }
    }
  }

  private fun addMostUsedApps() {
    if (mostUsedApps.isNotEmpty()) {
      header(R.string.most_used_apps) {
        id("most_used_apps_header")
      }
      for (app in mostUsedApps) {
        appInfo(app) {
          id(app.packageName)
        }
      }
    }
  }

}