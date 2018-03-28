package shaishav.com.bebetter.models

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import shaishav.com.bebetter.R
import shaishav.com.bebetter.data.models.Point
import shaishav.com.bebetter.utils.Constants
import shaishav.com.bebetter.viewholder.PointsTrendHolder
import java.util.*

/**
 * Created by shaishav.gandhi on 3/25/18.
 */
@EpoxyModelClass abstract class PointsTrendModel(val points: List<Point>): EpoxyModelWithHolder<PointsTrendHolder>() {

  override fun bind(holder: PointsTrendHolder) {
    super.bind(holder)

    val binding = holder.binding

    val xAxes = ArrayList<String>()
    val yAxes = ArrayList<Int>()


    for (point in points.reversed()) {
      val date = Date(point.date)
      xAxes.add(Constants.getFormattedDate(date))
      yAxes.add(point.points)
    }

    val data = ArrayList<ArrayList<Int>>()
    data.add(yAxes)

    binding?.pointsChart?.setBottomTextList(xAxes)
    binding?.pointsChart?.setDataList(data)
  }

  override fun getDefaultLayout(): Int {
    return R.layout.list_item_points_trend
  }
}