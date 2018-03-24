package shaishav.com.bebetter.extensions

import android.view.View

/**
 * Created by shaishav.gandhi on 3/23/18.
 */
fun View.getCenter(): Pair<Float, Float> {
  val cx = this.x + this.width / 2
  val cy = this.y + this.height / 2
  return Pair(cx, cy)
}