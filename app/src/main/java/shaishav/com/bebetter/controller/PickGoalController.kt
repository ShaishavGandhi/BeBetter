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
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import shaishav.com.bebetter.R
import shaishav.com.bebetter.contracts.PickGoalContract
import shaishav.com.bebetter.di.DependencyGraph
import shaishav.com.bebetter.presenter.PickGoalPresenter
import javax.inject.Inject
import shaishav.com.bebetter.extensions.getCenter
import java.util.*

/**
 * Created by shaishav.gandhi on 3/1/18.
 */
class PickGoalController: Controller(), PickGoalContract {

  lateinit var rootView: View
  lateinit var nextButton: Button
  lateinit var goalEditText: EditText
  lateinit var logo: ImageView
  lateinit var frame: View
  @Inject lateinit var presenter: PickGoalPresenter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    if (activity?.application is DependencyGraph) {
      (activity?.application as DependencyGraph).addPickGoalComponent(this).inject(this)
    }
    rootView = inflater.inflate(R.layout.fragment_pick_goal, container, false)
    initViews(rootView)
    rootView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
      override fun onLayoutChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int) {
        rootView.removeOnLayoutChangeListener(this)
        animateLogo()
      }

    })
    initListeners()
    return rootView
  }

  private fun animateLogo() {
    val animation = AnimationUtils.loadAnimation(activity, R.anim.linear)
    animation.apply {
      duration = 600
    }
    animation.setAnimationListener(object : Animation.AnimationListener{
      override fun onAnimationRepeat(p0: Animation?) {

      }

      override fun onAnimationEnd(p0: Animation?) {
        animateBackground()
      }

      override fun onAnimationStart(p0: Animation?) {
        logo.visibility = View.VISIBLE
      }

    })
    logo.startAnimation(animation)
  }

  private fun animateBackground() {
    val center = logo.getCenter()

    val anim = ViewAnimationUtils.createCircularReveal(frame, center.first.toInt(),
            center.second.toInt(), 0f, frame.width.toFloat())
            .apply {
              duration = 400
            }

    frame.visibility = View.VISIBLE
    anim.start()
  }

  private fun initViews(view: View) {
    nextButton = view.findViewById(R.id.nextButton)
    goalEditText = view.findViewById(R.id.goal)
    frame = view.findViewById(R.id.mainLayout)
    logo = view.findViewById(R.id.logo)
  }

  private fun initListeners() {
    nextButton.setOnClickListener {
      if (goalEditText.text.toString().isEmpty()) {
        Toast.makeText(activity, activity?.getString(R.string.goal_error), Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }
      val minutes = goalEditText.text.toString()
      presenter.saveGoal(Calendar.getInstance().timeInMillis, minutes.toInt())
    }
  }

  override fun homeScreen() {
    router.setRoot(
            RouterTransaction.with(HomeController())
                    .pushChangeHandler(FadeChangeHandler())
                    .popChangeHandler(FadeChangeHandler()))
  }

  override fun error() {
    Toast.makeText(activity, "Goal already exists.", Toast.LENGTH_SHORT).show()
    homeScreen()
  }

  override fun onDestroyView(view: View) {
    super.onDestroyView(view)
    presenter.unsubscribe()
  }
}