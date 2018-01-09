package shaishav.com.bebetter.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by shaishav.gandhi on 1/8/18.
 */
@Parcelize data class Usage(val id: Long, val date: Long, val usage: Long): Parcelable {
}