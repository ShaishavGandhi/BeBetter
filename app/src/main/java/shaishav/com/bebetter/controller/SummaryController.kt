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
import android.widget.Toast
import com.bluelinelabs.conductor.Controller
import shaishav.com.bebetter.R
import shaishav.com.bebetter.activities.MainActivity
import shaishav.com.bebetter.contracts.SummaryContract
import shaishav.com.bebetter.data.models.Summary
import shaishav.com.bebetter.di.DependencyGraph
import shaishav.com.bebetter.extensions.yesterday
import shaishav.com.bebetter.presenter.SummaryPresenter
import java.util.*
import javax.inject.Inject

class SummaryController(val date: Long): Controller(), SummaryContract {

  constructor() : this(Calendar.getInstance().yesterday().timeInMillis)

  companion object {
    val KEY = "summaryScreen"
  }

  lateinit var rootView: View
  @Inject lateinit var presenter: SummaryPresenter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    if (activity?.application is DependencyGraph) {
      (activity?.application as DependencyGraph).addSummaryComponent(this).inject(this)
    }
    (activity as MainActivity).setToolbarTitle("Summary")
    rootView = inflater.inflate(R.layout.controller_home, container, false)
    presenter.start()

    return rootView
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.detach()
    (activity?.application as DependencyGraph).removeSummaryComponent()
  }

  override fun setSummary(summary: Summary) {
    Toast.makeText(activity, summary.usage.usage.toString(), Toast.LENGTH_SHORT).show()
  }

}