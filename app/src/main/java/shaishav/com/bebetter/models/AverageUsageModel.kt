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

package shaishav.com.bebetter.models

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import shaishav.com.bebetter.R
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.extensions.toFormattedTime
import shaishav.com.bebetter.utils.ResourceManager
import shaishav.com.bebetter.viewholder.AverageUsageHolder

@EpoxyModelClass abstract class AverageUsageModel(val usage: Usage,
                                                  val averageUsage: Long,
                                                  val resourceManager: ResourceManager): EpoxyModelWithHolder<AverageUsageHolder>() {


  override fun bind(holder: AverageUsageHolder?) {
    super.bind(holder)

    if (usage.usage > averageUsage) {
      // User spent more time than average.
      val delta = usage.usage - averageUsage
      val stringRes = resourceManager.resources.getString(R.string.average_summary_over)
      val message = String.format(stringRes, usage.usage.toFormattedTime(), delta.toFormattedTime(),
              averageUsage.toFormattedTime())
      holder?.setUsageHighlight(message)

    } else {
      // User spent less time than average.
      val delta = averageUsage - usage.usage
      val stringRes = resourceManager.resources.getString(R.string.average_summary_under)
      val message = String.format(stringRes, usage.usage.toFormattedTime(), delta.toFormattedTime(),
              averageUsage.toFormattedTime())
      holder?.setUsageHighlight(message)
    }

  }

  override fun getDefaultLayout(): Int {
    return R.layout.list_item_average_usage
  }

}