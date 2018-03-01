package shaishav.com.bebetter.utils

import android.content.res.Resources

/**
 * Created by shaishav.gandhi on 2/25/18.
 */
class ResourceManager(val resources: Resources) {

  fun getString(id: Int): String {
    return resources.getString(id)
  }

}