package shaishav.com.bebetter.adapter

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import shaishav.com.bebetter.R
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.models.UsageCardModel_
import shaishav.com.bebetter.models.UsageTrendModel_
import shaishav.com.bebetter.utils.ResourceManager

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
class RecyclerUsageController(val resourceManager: ResourceManager) : EpoxyController() {

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

  override fun buildModels() {
    addCurrentSessionModel()
    addDailyUsage()
    addCurrentGoal()
    addUsageTrend()
    addCurrentStreak()
    addAverageDailyUsage()
    addTotalUsage()
  }

  private fun addCurrentSessionModel() {
    val header = resourceManager.getString(R.string.current_session)
    val footer = resourceManager.getString(R.string.minute)

    if (currentUsage > -1) {
      UsageCardModel_(header, currentUsage, footer)
              .id("current_usage_$currentUsage")
              .addTo(this)
    }
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
              .id("total_points")
              .addTo(this)
    }
  }

  private fun addUsageTrend() {
    if (usages.isNotEmpty() && goals.isNotEmpty()) {
      UsageTrendModel_(usages, goals)
              .id("usage_trend")
              .addTo(this)
    }
  }
}