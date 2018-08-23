/*
 *  Copyright (c) 2018 Shaishav Gandhi
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
 *
 */

package shaishav.com.bebetter.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import com.bluelinelabs.conductor.autodispose.ControllerScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import shaishav.com.bebetter.R
import shaishav.com.bebetter.activities.MainActivity
import shaishav.com.bebetter.adapter.RecyclerStatisticsController
import shaishav.com.bebetter.di.DependencyGraph
import shaishav.com.bebetter.listener.ActivityInteractor
import shaishav.com.bebetter.presenter.StatisticsPresenter
import javax.inject.Inject

class StatisticsController: BaseController() {

  lateinit var rootView: View
  lateinit var progressBar: ProgressBar
  lateinit var recyclerView: EpoxyRecyclerView
  lateinit var adapter: RecyclerStatisticsController

  @Inject lateinit var presenter: StatisticsPresenter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    if (activity?.application is DependencyGraph) {
      (activity?.application as DependencyGraph).addStatisticsComponent().inject(this)
    } else {
      throw IllegalStateException("Application must implement DependencyGraph")
    }
    rootView = inflater.inflate(R.layout.fragment_statistics, container, false)

    initViews(rootView.context)

    adapter = RecyclerStatisticsController()
    recyclerView.itemAnimator = SlideInUpAnimator()
    recyclerView.setController(adapter)
    return rootView
  }

  override fun onAttach(view: View) {
    super.onAttach(view)
    interactor?.setToolbarTitle(R.string.statistics)

    presenter.usageStats(System.currentTimeMillis())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(ControllerScopeProvider.from(this))
            .subscribe({
              recyclerView.visibility = View.VISIBLE
              progressBar.visibility = View.GONE
              adapter.usageStats = it
            }, {})
  }

  private fun initViews(context: Context) {
    recyclerView = rootView.findViewById(R.id.recyclerView)
    progressBar = rootView.findViewById(R.id.progressBar)
    recyclerView.layoutManager = LinearLayoutManager(context)
  }

  override fun shouldShowBottomNav(): Boolean {
    return true
  }
}
