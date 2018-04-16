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

package shaishav.com.bebetter.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import shaishav.com.bebetter.R
import shaishav.com.bebetter.extensions.yesterday
import java.util.*

class SummaryController(val date: Long): Controller() {

  constructor() : this(Calendar.getInstance().yesterday().timeInMillis)

  companion object {
    val KEY = "summaryScreen"
  }

  lateinit var rootView: View

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    rootView = inflater.inflate(R.layout.controller_home, container, false)

    return rootView
  }

}