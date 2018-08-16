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

package shaishav.com.bebetter.viewholder

import androidx.databinding.DataBindingUtil
import androidx.core.content.res.ResourcesCompat
import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import shaishav.com.bebetter.R
import shaishav.com.bebetter.data.models.Summary
import shaishav.com.bebetter.databinding.ListItemSummaryBinding
import shaishav.com.bebetter.extensions.toFormattedTime

class SummaryHolder : EpoxyHolder() {

  var binding: ListItemSummaryBinding? = null

  override fun bindView(itemView: View) {
    binding = DataBindingUtil.bind(itemView)

    binding?.let { binding ->
      val exoFont = ResourcesCompat.getFont(binding.root.context, R.font.exo_2)
      binding.usageLabel.typeface = exoFont
      binding.goalLabel.typeface = exoFont
      binding.pointsLabel.typeface = exoFont
    }
  }

  fun setData(summary: Summary) {
    binding?.let { binding ->
      binding.usageValue.text = summary.usage.usage.toFormattedTime()
      binding.goalValue.text = summary.goal.goal.toFormattedTime()
      binding.pointsValue.text = summary.point.points.toString()
    }

  }

}