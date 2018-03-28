package shaishav.com.bebetter.viewholder

import android.databinding.DataBindingUtil
import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import shaishav.com.bebetter.databinding.ListItemUsageCardBinding

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
class UsageViewHolder : EpoxyHolder() {

    var binding: ListItemUsageCardBinding? = null

    override fun bindView(itemView: View) {
        binding = DataBindingUtil.bind(itemView)
    }
}