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

import android.databinding.DataBindingUtil
import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import shaishav.com.bebetter.databinding.ListItemAverageUsageBinding

class AverageUsageHolder: EpoxyHolder() {

  var binding: ListItemAverageUsageBinding? = null

  override fun bindView(itemView: View) {
    binding = DataBindingUtil.bind(itemView)
  }

  fun setUsageHighlight(highlight: String) {
    binding?.usageGroup?.visibility = View.VISIBLE
    binding?.usageHighlight?.text = highlight
  }

  fun setPointsHighlight(highlight: String) {
    binding?.pointsGroup?.visibility = View.VISIBLE
    binding?.pointsHighlight?.text = highlight
  }


}