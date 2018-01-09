package shaishav.com.bebetter.adapter

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import shaishav.com.bebetter.R
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.models.UsageCardModel_
import shaishav.com.bebetter.models.UsageTrendModel_

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
class RecyclerUsageController(val context: Context): EpoxyController() {

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
    var totalUsage: Long = 0
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

    override fun buildModels() {
        addCurrentSessionModel()
        addDailyUsage()
        addAverageDailyUsage()
        addTotalUsage()
        addUsageTrend()
    }

    private fun addCurrentSessionModel() {
        val header = context.getString(R.string.current_session)
        val footer = context.getString(R.string.minute)

        if (currentUsage > -1) {
            UsageCardModel_(header, currentUsage, footer)
                    .id("current_usage")
                    .addTo(this)
        }
    }

    private fun addDailyUsage() {
        val header = context.getString(R.string.daily_usage)
        val footer = context.getString(R.string.minute)

        if (dailyUsage > -1) {
            UsageCardModel_(header, dailyUsage, footer)
                    .id("daily_usage")
                    .addTo(this)
        }
    }

    private fun addAverageDailyUsage() {
        val header = context.getString(R.string.average_daily_usage)
        val footer = context.getString(R.string.minute)

        if (averageDailyUsage > -1) {
            UsageCardModel_(header, averageDailyUsage, footer)
                    .id("average_daily_usage")
                    .addTo(this)
        }
    }

    private fun addTotalUsage() {
        val header = context.getString(R.string.total_usage)
        val footer = context.getString(R.string.minute)

        if (totalUsage > -1) {
            UsageCardModel_(header, totalUsage, footer)
                    .id("total_usage")
                    .addTo(this)
        }
    }

    private fun addTotalPoints() {
        val header = context.getString(R.string.total_points)
        val footer = context.getString(R.string.points)

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