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

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyRecyclerView
import com.bluelinelabs.conductor.Controller
import com.github.jinatonic.confetti.CommonConfetti
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import shaishav.com.bebetter.R
import shaishav.com.bebetter.activities.MainActivity
import shaishav.com.bebetter.adapter.RecyclerSummaryController
import shaishav.com.bebetter.contracts.SummaryContract
import shaishav.com.bebetter.data.models.Summary
import shaishav.com.bebetter.di.DependencyGraph
import shaishav.com.bebetter.extensions.yesterday
import shaishav.com.bebetter.presenter.SummaryPresenter
import shaishav.com.bebetter.utils.ResourceManager
import java.util.*
import javax.inject.Inject

class SummaryController(val date: Long): Controller(), SummaryContract {

  constructor() : this(Calendar.getInstance().yesterday().timeInMillis)

  companion object {
    val KEY = "summaryScreen"
  }

  lateinit var rootView: View
  lateinit var recyclerView: EpoxyRecyclerView
  lateinit var adapter: RecyclerSummaryController
  @Inject lateinit var presenter: SummaryPresenter
  @Inject lateinit var resourceManager: ResourceManager

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    if (activity?.application is DependencyGraph) {
      (activity?.application as DependencyGraph).addSummaryComponent(this).inject(this)
    }
    (activity as MainActivity).setToolbarTitle("Summary")
    rootView = inflater.inflate(R.layout.controller_summary, container, false)
    initViews(rootView.context)

    adapter = RecyclerSummaryController(resourceManager)
    recyclerView.itemAnimator = SlideInUpAnimator()
    recyclerView.setController(adapter)

    presenter.start(date)
    presenter.averageUsage()
    presenter.averagePoints()
    presenter.streak()

    return rootView
  }

  private fun initViews(context: Context) {
    recyclerView = rootView.findViewById(R.id.recyclerView)
    recyclerView.layoutManager = LinearLayoutManager(context)
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.detach()
    (activity?.application as DependencyGraph).removeSummaryComponent()
  }

  override fun setSummary(summary: Summary) {
    adapter.summary = summary
  }

  override fun setGoalAchieved() {
    adapter.goalAchieved = true
    resources?.let {
      CommonConfetti.rainingConfetti(rootView as ViewGroup, intArrayOf(it.getColor(R.color.colorPrimary)))
              .stream(6000)
    }
  }

  override fun setStreak(streak: Long) {
    adapter.streak = streak
  }

  override fun setAverageUsage(averageUsage: Long) {
    // TODO: Remove multiplication after all calls to it are refactored
    adapter.averageUsage = averageUsage * 1000 * 60
  }

  override fun setAveragePoints(averagePoints: Int) {
    adapter.averagePoints = averagePoints
  }

}