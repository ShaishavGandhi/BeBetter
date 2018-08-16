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

import androidx.core.content.res.ResourcesCompat
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import shaishav.com.bebetter.R
import shaishav.com.bebetter.data.models.Point
import shaishav.com.bebetter.utils.Constants
import shaishav.com.bebetter.viewholder.PointsTrendHolder
import java.util.*

/**
 * Created by shaishav.gandhi on 3/25/18.
 */
@EpoxyModelClass abstract class PointsTrendModel(val points: List<Point>): EpoxyModelWithHolder<PointsTrendHolder>() {

  override fun bind(holder: PointsTrendHolder) {
    super.bind(holder)

    val binding = holder.binding
    binding?.apply {
      val context = root.context
      val redColor = context.resources.getColor(R.color.red)

      chart.xAxis.apply {
        setDrawAxisLine(false)
        setDrawLabels(false)
      }

      chart.description.isEnabled = false
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

      val pointsEntries = ArrayList<Entry>()
      val labels = arrayListOf<String>()

      for ((index, point) in points.reversed().withIndex()) {
        val date = Date(point.date)
        labels.add(Constants.getFormattedDate(date))
        val entry = Entry(index.toFloat(), point.points.toFloat() / (1000 * 60))
        pointsEntries.add(entry)
      }

      chart.xAxis.setValueFormatter { value, _ ->
        if (value.toInt() < labels.size) {
          return@setValueFormatter labels[value.toInt()]
        } else {
          ""
        }
      }
      chart.xAxis.labelCount = labels.size

      val pointsDataSet: LineDataSet
      // create a dataset and give it a type
      pointsDataSet = LineDataSet(pointsEntries, "")
      pointsDataSet.setDrawIcons(false)
      pointsDataSet.lineWidth = 2f
      pointsDataSet.color = redColor
      pointsDataSet.circleHoleRadius = 2f
      pointsDataSet.circleRadius = 5f
      pointsDataSet.setCircleColor(redColor)
      pointsDataSet.valueTextSize = 12f
      pointsDataSet.setDrawValues(false)
      pointsDataSet.valueTypeface = ResourcesCompat.getFont(context, R.font.exo_2)

      chart.xAxis.apply {
        setLabelCount(points.size, true)
      }

      val dataSets = ArrayList<ILineDataSet>()
      dataSets.add(pointsDataSet) // add the datasets

      // create a data object with the datasets
      val data = LineData(dataSets)

      // set data
      chart.data = data
      chart.setVisibleXRangeMaximum(6.0f)
      // Scroll to last value
      chart.moveViewToX(Float.MAX_VALUE)
    }
  }

  override fun getDefaultLayout(): Int {
    return R.layout.list_item_points_trend
  }
}
