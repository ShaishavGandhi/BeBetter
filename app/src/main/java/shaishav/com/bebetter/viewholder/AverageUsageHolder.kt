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
import android.text.Spannable
import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import shaishav.com.bebetter.databinding.ListItemAverageUsageBinding

class AverageUsageHolder: EpoxyHolder() {

  var binding: ListItemAverageUsageBinding? = null

  override fun bindView(itemView: View) {
    binding = DataBindingUtil.bind(itemView)
  }

  fun setUsageHighlight(highlight: Spannable) {
    binding?.usageGroup?.visibility = View.VISIBLE
    binding?.usageHighlight?.text = highlight
  }

  fun setPointsHighlight(highlight: Spannable) {
    binding?.pointsGroup?.visibility = View.VISIBLE
    binding?.pointsHighlight?.text = highlight
  }

  fun setStreakHighlight(highlight: Spannable) {
    binding?.streakGroup?.visibility = View.VISIBLE
    binding?.streakHighlight?.text = highlight
  }

  fun setStreakHighlight(highlight: String) {
    binding?.streakGroup?.visibility = View.VISIBLE
    binding?.streakHighlight?.text = highlight
  }


}