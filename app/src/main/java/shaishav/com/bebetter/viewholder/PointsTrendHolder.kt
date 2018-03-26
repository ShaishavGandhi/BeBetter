package shaishav.com.bebetter.viewholder

import android.databinding.DataBindingUtil
import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import shaishav.com.bebetter.databinding.ListItemPointsTrendBinding

/**
 * Created by shaishav.gandhi on 3/25/18.
 */
class PointsTrendHolder: EpoxyHolder() {

  lateinit var binding: ListItemPointsTrendBinding

  override fun bindView(itemView: View) {
    binding = DataBindingUtil.bind(itemView)
  }
}