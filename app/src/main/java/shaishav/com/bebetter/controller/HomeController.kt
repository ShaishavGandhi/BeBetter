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
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import shaishav.com.bebetter.R
import shaishav.com.bebetter.adapter.RecyclerUsageController
import shaishav.com.bebetter.contracts.HomeContract
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.data.models.Level
import shaishav.com.bebetter.data.models.Point
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.di.DependencyGraph
import shaishav.com.bebetter.di.modules.SummaryModule
import shaishav.com.bebetter.listener.SummaryListener
import shaishav.com.bebetter.presenter.HomePresenter
import shaishav.com.bebetter.utils.ResourceManager
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
class HomeController : Controller(), HomeContract, SummaryListener {

  lateinit var rootView: View
  lateinit var recyclerView: EpoxyRecyclerView
  lateinit var adapter: RecyclerUsageController

  @Inject lateinit var presenter: HomePresenter
  @Inject lateinit var resources: ResourceManager

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    if (activity?.application is DependencyGraph) {
      (activity?.application as DependencyGraph).addSummaryComponent(SummaryModule(this)).inject(this)
    }
    rootView = inflater.inflate(R.layout.fragment_day_summary, container, false)
    presenter.view = this
    initViews(rootView.context)

    adapter = RecyclerUsageController(resources, this)
    recyclerView.itemAnimator = SlideInUpAnimator()
    recyclerView.setController(adapter)
    return rootView
  }

  private fun initViews(context: Context) {
    recyclerView = rootView.findViewById(R.id.recyclerView)
    recyclerView.layoutManager = LinearLayoutManager(context)
  }

  override fun onDestroy() {
    super.onDestroy()
    if (activity?.application is DependencyGraph) {
      (activity?.application as DependencyGraph).removeSummaryComponent()
    }
    presenter.unsubscribe()
  }

  override fun onEditGoal() {
    router.pushController(
            RouterTransaction.with(EditGoalController())
                    .pushChangeHandler(FadeChangeHandler())
                    .popChangeHandler(FadeChangeHandler()))
  }

  override fun setAverageDaiyUsage(usage: Long) {
    adapter.averageDailyUsage = usage
  }

  override fun setDailyUsage(usage: Long) {
    adapter.dailyUsage = usage
  }

  override fun setTotalUsage(usage: Long) {
    adapter.totalUsage = usage
  }

  override fun setGoals(goals: List<Goal>) {
    adapter.goals = goals
  }

  override fun setCurrentStreak(currentStreak: Long) {
    adapter.currentStreak = currentStreak
  }

  override fun setUsages(usages: List<Usage>) {
    adapter.usages = usages
  }

  override fun setCurrentGoal(goal: Goal) {
    adapter.currentGoal = goal.goal / (1000 * 60)
  }

  override fun setTotalPoints(totalPoints: Long) {
    adapter.totalPoints = totalPoints
  }

  override fun setPoints(points: List<Point>) {
    adapter.points = points
  }

  override fun setLevel(level: Level) {
    adapter.level = level
  }
}