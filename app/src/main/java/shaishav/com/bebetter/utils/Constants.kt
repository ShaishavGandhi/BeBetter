package shaishav.com.bebetter.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by shaishav.gandhi on 2/25/18.
 */
class Constants {

  companion object {
    @JvmField val PACKAGE = "shaishav.com.bebetter"
    @JvmField val PREFERENCES = "com.bebetter.com"

    @JvmStatic fun getFormattedDate(date: Date): String {
      val dateFormat = SimpleDateFormat("MMM dd")
      return dateFormat.format(date)
    }
  }

}