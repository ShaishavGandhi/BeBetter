package shaishav.com.bebetter.data.models

import android.content.ContentValues
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import shaishav.com.bebetter.data.contracts.PointContract

/**
 * Created by shaishav.gandhi on 1/8/18.
 */
@Parcelize data class Point(val date: Long, val id: Long, val points: Int): Parcelable {

  fun toContentValues(): ContentValues {
    return ContentValues().apply {
      put(PointContract.COLUMN_DATE, date)
      put(PointContract.COLUMN_POINTS, points)
    }
  }
}