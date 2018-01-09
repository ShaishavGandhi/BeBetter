package shaishav.com.bebetter.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by shaishav.gandhi on 1/8/18.
 */
@Parcelize data class Point(val date: Long, val id: Long, val points: Int): Parcelable {
}