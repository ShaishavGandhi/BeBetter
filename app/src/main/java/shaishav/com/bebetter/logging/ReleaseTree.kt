/*
 * Copyright (c) 2018 Shaishav Gandhi
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions
 *  and limitations under the License.
 */

package shaishav.com.bebetter.logging

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

/**
 * Created by shaishav.gandhi on 3/25/18.
 */
class ReleaseTree: Timber.Tree() {

  private val CRASHLYTICS_KEY_PRIORITY = "priority"
  private val CRASHLYTICS_KEY_TAG = "tag"
  private val CRASHLYTICS_KEY_MESSAGE = "message"

  override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
    if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
      return;
    }

    Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY, priority)
    Crashlytics.setString(CRASHLYTICS_KEY_TAG, tag)
    Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE, message)

    if (t == null) {
      Crashlytics.logException(Exception(message))
    } else {
      Crashlytics.logException(t)
    }
  }
}