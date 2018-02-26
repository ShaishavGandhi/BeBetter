package shaishav.com.bebetter.data.models

import android.content.ContentValues
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import shaishav.com.bebetter.data.contracts.GoalContract

/**
 * Created by shaishav.gandhi on 1/5/18.
 */
@Parcelize data class Goal(val id: Long, val date: Long, val goal: Long): Parcelable {

  fun toContentValues(): ContentValues {
    val contentValues = ContentValues()
    contentValues.put(GoalContract.COLUMN_GOAL, goal)
    contentValues.put(GoalContract.COLUMN_DATE, date)
    return contentValues
  }

}