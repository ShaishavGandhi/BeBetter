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
import shaishav.com.bebetter.data.models.Summary
import shaishav.com.bebetter.viewholder.SummaryHolder

@EpoxyModelClass abstract class SummaryModel(val summary: Summary): EpoxyModelWithHolder<SummaryHolder>() {

  override fun bind(holder: SummaryHolder?) {
    super.bind(holder)
    holder?.setData(summary)
  }

  override fun getDefaultLayout(): Int {
    return R.layout.list_item_summary
  }
}
