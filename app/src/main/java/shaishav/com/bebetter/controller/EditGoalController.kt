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
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.autodispose.ControllerScopeProvider
import com.bluelinelabs.conductor.changehandler.AutoTransitionChangeHandler
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import shaishav.com.bebetter.R
import shaishav.com.bebetter.activities.MainActivity
import shaishav.com.bebetter.contracts.PickGoalContract
import shaishav.com.bebetter.di.DependencyGraph
import shaishav.com.bebetter.presenter.PickGoalPresenter
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 3/19/18.
 */
class EditGoalController: Controller(), PickGoalContract {

  lateinit var rootView: View
  lateinit var nextButton: Button
  lateinit var goalEditText: EditText
  @Inject lateinit var presenter: PickGoalPresenter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    if (activity?.application is DependencyGraph) {
      (activity?.application as DependencyGraph).addPickGoalComponent(this,
              ControllerScopeProvider.from(this)).inject(this)
    }
    (activity as MainActivity).setToolbarTitle(activity?.resources?.getString(R.string.edit_goal)!!)
    rootView = inflater.inflate(R.layout.fragment_edit_goal, container, false)
    initViews(rootView)
    initListeners()
    return rootView
  }

  private fun initViews(view: View) {
    nextButton = view.findViewById(R.id.nextButton)
    goalEditText = view.findViewById(R.id.goal)
  }

  private fun initListeners() {
    nextButton.setOnClickListener {
      if (goalEditText.text.toString().isEmpty()) {
        Toast.makeText(activity, activity?.getString(R.string.goal_error), Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }
      val minutes = goalEditText.text.toString()
      val calendar = Calendar.getInstance()
      calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1)
      presenter.saveGoal(calendar.timeInMillis, minutes.toInt())
    }
  }

  override fun homeScreen() {
    router.popCurrentController()
  }

  override fun error() {
    Toast.makeText(activity, "Goal already exists for that day.", Toast.LENGTH_SHORT).show()
    homeScreen()
  }

  override fun onDestroyView(view: View) {
    super.onDestroyView(view)
    presenter.unsubscribe()
  }
}