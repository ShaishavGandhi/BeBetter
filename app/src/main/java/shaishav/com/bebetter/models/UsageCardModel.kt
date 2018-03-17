package shaishav.com.bebetter.models

import android.support.v4.content.res.ResourcesCompat
import android.view.Gravity
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.robinhood.ticker.TickerUtils
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

        val exoFont = ResourcesCompat.getFont(binding.root.context, R.font.exo_2)

        holder.binding.value.apply {
            setCharacterList(TickerUtils.getDefaultNumberList())
            animationDuration = 800
            text = usage.toString()
            typeface = exoFont
        }

        binding.executePendingBindings()
    }

    override fun getDefaultLayout(): Int {
        return R.layout.list_item_usage_card
    }
}