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

package shaishav.com.bebetter.adapter

import com.airbnb.epoxy.EpoxyController
import shaishav.com.bebetter.R
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.data.models.Level
import shaishav.com.bebetter.data.models.Point
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.listener.SummaryListener
import shaishav.com.bebetter.models.LevelModel_
import shaishav.com.bebetter.models.PointsTrendModel_
import shaishav.com.bebetter.models.UsageCardModel_
import shaishav.com.bebetter.models.UsageTrendModel_
import shaishav.com.bebetter.utils.ResourceManager

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
class RecyclerUsageController(val resourceManager: ResourceManager, val listener: SummaryListener) : EpoxyController() {

  var currentUsage: Long = -1
    set(value) {
      field = value
      requestModelBuild()
    }
  var dailyUsage: Long = -1
    set(value) {
      field = value
      requestModelBuild()
    }
  var averageDailyUsage: Long = -1
    set(value) {
      field = value
      requestModelBuild()
    }
  var totalUsage: Long = -1
    set(value) {
      field = value
      requestModelBuild()
    }

  var totalPoints: Long = -1
    set(value) {
      field = value
      requestModelBuild()
    }

  var usages: List<Usage> = ArrayList()
    set(value) {
      field = value
      requestModelBuild()
    }

  var goals: List<Goal> = ArrayList()
    set(value) {
      field = value
      requestModelBuild()
    }

  var points: List<Point> = ArrayList()
  set(value) {
    field = value
    requestModelBuild()
  }

  var currentStreak: Long = -1
    set(value) {
      field = value
      requestModelBuild()
    }

  var currentGoal: Long = -1
  set(value) {
    field = value
    requestModelBuild()
  }

  var level: Level? = null
  set(value) {
    field = value
    requestModelBuild()
  }

  override fun buildModels() {
    addDailyUsage()
    addCurrentGoal()
    addUsageTrend()
    addCurrentStreak()
    addTotalPoints()
    addLevel()
    addPointsStreak()
    addAverageDailyUsage()
    addTotalUsage()
  }

  private fun addCurrentStreak() {
    val header = resourceManager.getString(R.string.current_streak)
    val footer = resourceManager.getString(R.string.day)

    if (currentStreak > -1) {
      UsageCardModel_(header, currentStreak, footer)
              .id("current_streak_$currentStreak")
              .addTo(this)
    }
  }

  private fun addDailyUsage() {
    val header = resourceManager.getString(R.string.daily_usage)
    val footer = resourceManager.getString(R.string.minute)

    if (dailyUsage > -1) {
      UsageCardModel_(header, dailyUsage, footer)
              .id("daily_usage_$dailyUsage")
              .addTo(this)
    }
  }

  private fun addAverageDailyUsage() {
    val header = resourceManager.getString(R.string.average_daily_usage)
    val footer = resourceManager.getString(R.string.minute)

    if (averageDailyUsage > -1) {
      UsageCardModel_(header, averageDailyUsage, footer)
              .id("average_daily_usage_$averageDailyUsage")
              .addTo(this)
    }
  }

  private fun addCurrentGoal() {
    val header = resourceManager.getString(R.string.current_goal)
    val footer = resourceManager.getString(R.string.minute)

    if (currentGoal > -1) {
      UsageCardModel_(header, currentGoal, footer)
              .id("current_goal_$currentGoal")
              .addListener(listener)
              .addTo(this)

    }
  }

  private fun addTotalUsage() {
    val header = resourceManager.getString(R.string.total_usage)
    val footer = resourceManager.getString(R.string.minute)

    if (totalUsage > -1) {
      UsageCardModel_(header, totalUsage, footer)
              .id("total_usage_$totalUsage")
              .addTo(this)
    }
  }

  private fun addTotalPoints() {
    val header = resourceManager.getString(R.string.total_points)
    val footer = resourceManager.getString(R.string.points)

    if (totalPoints > -1) {
      UsageCardModel_(header, totalPoints, footer)
              .id("total_points_$totalPoints")
              .addTo(this)
    }
  }

  private fun addLevel() {
    level?.let { level ->
      LevelModel_(level)
              .id("level_$level")
              .addTo(this)
    }
  }

  private fun addUsageTrend() {
    if (usages.isNotEmpty() && goals.isNotEmpty()) {
      UsageTrendModel_(usages, goals)
              .id("usage_trend${usages.size}_${goals.size}")
              .addTo(this)
    }
  }

  private fun addPointsStreak() {
    if (points.isNotEmpty()) {
      PointsTrendModel_(points)
              .id("points_trend_${points.size}")
              .addTo(this)
    }
  }
}