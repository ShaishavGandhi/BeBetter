package shaishav.com.bebetter.models

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import shaishav.com.bebetter.R
import shaishav.com.bebetter.viewholder.UsageViewHolder

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
@EpoxyModelClass
abstract class UsageCardModel(val header: String, val usage: Long, val footer: String): EpoxyModelWithHolder<UsageViewHolder>() {

    override fun bind(holder: UsageViewHolder) {
        super.bind(holder)

        val binding = holder.binding
        with (binding) {
            title = header
            this.usage = this@UsageCardModel.usage.toString()
            this.footer = this@UsageCardModel.footer
        }

        binding.executePendingBindings()
    }

    override fun getDefaultLayout(): Int {
        return R.layout.list_item_usage_card
    }
}