package shaishav.com.bebetter.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.AutoTransitionChangeHandler
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import shaishav.com.bebetter.R
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
      (activity?.application as DependencyGraph).addPickGoalComponent(this).inject(this)
    }
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
      val minutes = goalEditText.text.toString()
      val calendar = Calendar.getInstance()
      calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1)
      presenter.saveGoal(calendar.timeInMillis, minutes.toInt())
    }
  }

  override fun homeScreen() {
    router.pushController(
            RouterTransaction.with(SummaryController())
                    .pushChangeHandler(AutoTransitionChangeHandler())
                    .popChangeHandler(AutoTransitionChangeHandler()))
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