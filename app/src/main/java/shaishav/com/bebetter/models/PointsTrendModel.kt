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

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
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

    val xAxes = ArrayList<String>()
    val yAxes = ArrayList<Int>()


    for (point in points.reversed()) {
      val date = Date(point.date)
      xAxes.add(Constants.getFormattedDate(date))
      yAxes.add(point.points)
    }

    val data = ArrayList<ArrayList<Int>>()
    data.add(yAxes)

    binding?.pointsChart?.setBottomTextList(xAxes)
    binding?.pointsChart?.setDataList(data)
  }

  override fun getDefaultLayout(): Int {
    return R.layout.list_item_points_trend
  }
}