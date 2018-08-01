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

package shaishav.com.bebetter.models

import android.graphics.Color
import android.support.v4.content.res.ResourcesCompat
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import shaishav.com.bebetter.R
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.viewholder.UsageTrendViewHolder
import java.util.*
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import shaishav.com.bebetter.extensions.toFormattedTime
import shaishav.com.bebetter.utils.Constants
import kotlin.collections.ArrayList


/**
 * Created by shaishav.gandhi on 12/25/17.
 */
@EpoxyModelClass
abstract class UsageTrendModel(val usages: List<Usage>, val goals: List<Goal>) : EpoxyModelWithHolder<UsageTrendViewHolder>() {

  override fun bind(holder: UsageTrendViewHolder) {
    super.bind(holder)

    val binding = holder.binding
    binding?.apply {
      val context = root.context
      val redColor = context.resources.getColor(R.color.red)
      val blueColor = context.resources.getColor(R.color.blue)

      chart.xAxis.apply {
        setDrawAxisLine(false)
        setDrawLabels(false)
      }

      chart.axisLeft.apply {
        setDrawAxisLine(false)
        setDrawLabels(false)
      }

      chart.axisRight.apply {
        setDrawAxisLine(false)
        setDrawGridLines(false)
        setDrawLabels(false)
      }

      chart.legend.apply {
        isEnabled = false
      }

      val usageEntries = ArrayList<Entry>()
      val goalEntries = ArrayList<Entry>()

      val labels = arrayListOf<String>()

      for ((actualIndex, index) in (usages.size - 1 downTo 0).withIndex()) {
        val usage = usages[index]
        val entry = Entry(actualIndex.toFloat(), usage.usage.toFloat() / (1000 * 60))
        usageEntries.add(entry)
      }

      for ((actualIndex, index) in (goals.size - 1 downTo 0).withIndex()) {
        val goal = goals[index]

        val entry = Entry(actualIndex.toFloat(), goal.goal.toFloat() / (1000 * 60))
        goalEntries.add(entry)
        labels.add(Constants.getFormattedDate(Date(goal.date)))
      }

      chart.xAxis.setValueFormatter { value, _ ->
        if (value.toInt() < labels.size) {
          return@setValueFormatter labels[value.toInt()]
        }
        return@setValueFormatter ""
      }
      chart.description.isEnabled = false
      chart.xAxis.labelCount = labels.size

      val usageDataSet: LineDataSet
      // create a dataset and give it a type
      usageDataSet = LineDataSet(usageEntries, "")
      usageDataSet.setDrawIcons(false)
      usageDataSet.lineWidth = 2f
      usageDataSet.color = redColor
      usageDataSet.circleHoleRadius = 2f
      usageDataSet.circleRadius = 5f
      usageDataSet.setCircleColor(redColor)
      usageDataSet.valueTextSize = 12f
      usageDataSet.setDrawValues(false)
      usageDataSet.valueTypeface = ResourcesCompat.getFont(context, R.font.exo_2)

      val goalDataSet: LineDataSet
      // create a dataset and give it a type
      goalDataSet = LineDataSet(goalEntries, "")
      goalDataSet.setDrawIcons(false)
      goalDataSet.lineWidth = 2f
      goalDataSet.color = blueColor
      goalDataSet.setCircleColor(blueColor)
      goalDataSet.valueTextSize = 12f
      goalDataSet.valueTypeface = ResourcesCompat.getFont(context, R.font.exo_2)
      goalDataSet.circleHoleRadius = 2f
      goalDataSet.circleRadius = 5f
      goalDataSet.setDrawValues(false)

      chart.xAxis.apply {
        setLabelCount(Math.max(goals.size, usages.size), true)
      }

      val dataSets = ArrayList<ILineDataSet>()
      dataSets.add(usageDataSet) // add the datasets
      dataSets.add(goalDataSet)

      // create a data object with the datasets
      val data = LineData(dataSets)

      // set data
      chart.data = data
      chart.setVisibleXRangeMaximum(6.0f)
    }
  }

  override fun getDefaultLayout(): Int {
    return R.layout.list_item_usage_trend
  }
}
