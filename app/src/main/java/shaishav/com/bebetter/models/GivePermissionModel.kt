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
import androidx.databinding.DataBindingUtil
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import shaishav.com.bebetter.R
import shaishav.com.bebetter.databinding.ListItemGivePermissionBinding

@EpoxyModelClass(layout = R.layout.list_item_give_permission)
abstract class GivePermissionModel(): EpoxyModelWithHolder<GivePermissionHolder>()  {

@EpoxyAttribute lateinit var listener: () -> Unit

  override fun bind(holder: GivePermissionHolder) {
    super.bind(holder)
    holder.binding?.givePermission?.setOnClickListener {
      listener()
    }
  }
}

class GivePermissionHolder: EpoxyHolder() {
  var binding: ListItemGivePermissionBinding? = null

  override fun bindView(itemView: View) {
    binding = DataBindingUtil.bind(itemView)
  }

}
