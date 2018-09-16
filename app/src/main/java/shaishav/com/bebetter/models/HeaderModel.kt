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

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import kotlinx.android.synthetic.main.list_item_header.view.*
import shaishav.com.bebetter.R

@EpoxyModelClass(layout = R.layout.list_item_header)
abstract class HeaderModel(@StringRes val resId: Int): EpoxyModelWithHolder<HeaderHolder>() {

  override fun bind(holder: HeaderHolder) {
    super.bind(holder)
    holder.headerView.setText(resId)
  }
}

class HeaderHolder: EpoxyHolder() {

  lateinit var headerView: TextView

  override fun bindView(itemView: View) {
    headerView = itemView.header
  }

}
