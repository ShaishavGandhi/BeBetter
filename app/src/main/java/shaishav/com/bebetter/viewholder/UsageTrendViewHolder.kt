package shaishav.com.bebetter.viewholder

import android.databinding.DataBindingUtil
import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import shaishav.com.bebetter.databinding.ListItemUsageTrendBinding

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
class UsageTrendViewHolder: EpoxyHolder() {

    var binding: ListItemUsageTrendBinding? = null

    override fun bindView(itemView: View) {
        binding = DataBindingUtil.bind(itemView)
    }
}