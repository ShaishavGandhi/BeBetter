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

package shaishav.com.bebetter.utils

import android.content.res.Resources
import shaishav.com.bebetter.R
import shaishav.com.bebetter.data.models.Level

object UIUtils {

  fun getLevel(level: Level, resources: Resources): String {
    return when (level) {
      Level.BEGINNER -> resources.getString(R.string.beginner)
      Level.APPRENTICE -> resources.getString(R.string.apprentice)
      Level.INTERMEDIATE -> resources.getString(R.string.intermediate)
      Level.PRO -> resources.getString(R.string.pro)
      Level.MASTER -> resources.getString(R.string.master)
      Level.LEGEND -> resources.getString(R.string.legend)
      Level.ULTRA_LEGEND -> resources.getString(R.string.ultra_legend)
      Level.EXPERT -> resources.getString(R.string.expert)
    }
  }

}