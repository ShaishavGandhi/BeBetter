package shaishav.com.bebetter.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import shaishav.com.bebetter.R
import shaishav.com.bebetter.contracts.PickGoalContract
import shaishav.com.bebetter.di.DependencyGraph
import shaishav.com.bebetter.di.modules.SummaryModule
import shaishav.com.bebetter.presenter.PickGoalPresenter
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_pick_goal.*

/**
 * Created by shaishav.gandhi on 3/1/18.
 */
class PickGoalController: Controller(), PickGoalContract {

  lateinit var rootView: View
  lateinit var nextButton: Button
  lateinit var goalEditText: EditText
  @Inject lateinit var presenter: PickGoalPresenter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    if (activity?.application is DependencyGraph) {
      (activity?.application as DependencyGraph).addPickGoalComponent(this).inject(this)
    }
    rootView = inflater.inflate(R.layout.fragment_pick_goal, container, false)
    initViews(rootView)
    initListeners()
    return rootView
  }

  fun initViews(view: View) {
    nextButton = view.findViewById(R.id.nextButton)
    goalEditText = view.findViewById(R.id.goal)
  }

  fun initListeners() {
    nextButton.setOnClickListener {
      val minutes = goalEditText.text.toString()
      presenter.saveGoal(minutes.toInt())
    }
  }

  override fun homeScreen() {
    router.pushController(
            RouterTransaction.with(SummaryController())
                    .pushChangeHandler(FadeChangeHandler())
                    .popChangeHandler(FadeChangeHandler()))
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.unsubscribe()
  }
}