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
    @JvmField val FULL_NAME = "first_name"
    @JvmField val LOCKED = "locked"
    @JvmField val UNLOCKED = "unlocked"
    @JvmField val SESSION = "session"
    @JvmField val GOAL = "goal"
    @JvmField val PREFERENCE_USAGE_UNIT = "usage_unit"
    @JvmField val MEET_GOAL = 50

    @JvmStatic fun getFormattedDate(date: Date): String {
      val dateFormat = SimpleDateFormat("MMM dd")
      return dateFormat.format(date)
    }
  }

}