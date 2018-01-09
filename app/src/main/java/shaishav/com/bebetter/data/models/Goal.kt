package shaishav.com.bebetter.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by shaishav.gandhi on 1/5/18.
 */
@Parcelize data class Goal(val id: Long, val date: Long, val goal: Long): Parcelable {
}