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
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.viewholder.UsageTrendViewHolder
import shaishav.com.bebetter.utils.Constants
import java.util.*

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
@EpoxyModelClass
abstract class UsageTrendModel(val usages: List<Usage>, val goals: List<Goal>) : EpoxyModelWithHolder<UsageTrendViewHolder>() {

  override fun bind(holder: UsageTrendViewHolder) {
    super.bind(holder)

    val binding = holder.binding

    val xAxes = ArrayList<String>()
    val yAxes = ArrayList<Int>()
    val threshold = ArrayList<Int>()


    for (index in usages.size - 1 downTo 0) {
      if (index >= usages.size || index > goals.size - 1) {
        break
      }
      val usage = usages[index]
      val goal = goals[index]

      val date = Date(usage.date)
      xAxes.add(Constants.getFormattedDate(date))
      threshold.add(goal.goal.toInt() / (1000 * 60))
      yAxes.add(usage.usage.toInt() / (1000 * 60))
    }

    val data = ArrayList<ArrayList<Int>>()
    data.add(yAxes)
    data.add(threshold)

    binding?.usageChart?.setBottomTextList(xAxes)
    binding?.usageChart?.setDataList(data)
  }

  override fun getDefaultLayout(): Int {
    return R.layout.list_item_usage_trend
  }
}