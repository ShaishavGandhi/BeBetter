package shaishav.com.bebetter.viewholder

import android.databinding.DataBindingUtil
import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import shaishav.com.bebetter.databinding.ListItemUsageCardBinding

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
class UsageViewHolder : EpoxyHolder() {

    lateinit var binding: ListItemUsageCardBinding

    override fun bindView(itemView: View?) {
        binding = DataBindingUtil.bind(itemView)
    }
}