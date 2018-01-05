package shaishav.com.bebetter.models

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import shaishav.com.bebetter.R
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.viewholder.UsageTrendViewHolder
import shaishav.com.bebetter.utils.Constants
import java.util.*

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
@EpoxyModelClass
abstract class UsageTrendModel(val usages: List<Usage>, val goals: List<Goal>): EpoxyModelWithHolder<UsageTrendViewHolder>() {

    override fun bind(holder: UsageTrendViewHolder) {
        super.bind(holder)

        val binding = holder.binding

        val xAxes = ArrayList<String>()
        val yAxes = ArrayList<Int>()
        val threshold = ArrayList<Int>()


        for (index in usages.indices) {
            if (index >= usages.size || index >= goals.size) {
                break
            }
            val usage = usages[index]
            val goal = goals[index]

            val date = Date(usage.date)
            xAxes.add(Constants.getFormattedDate(date))
            threshold.add(goal.goal.toInt() / (1000 * 60))
            yAxes.add(usage.usage.toInt() / (1000 * 60))
        }

        val data = ArrayList<ArrayList<Int>>()
        data.add(yAxes)
        data.add(threshold)

        binding.usageChart.setBottomTextList(xAxes)
        binding.usageChart.setDataList(data)
    }

    override fun getDefaultLayout(): Int {
        return R.layout.list_item_usage_trend
    }
}