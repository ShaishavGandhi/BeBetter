package shaishav.com.bebetter.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.contracts.PickGoalContract
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.data.preferences.PreferenceDataStore
import shaishav.com.bebetter.data.repository.GoalRepository
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 3/1/18.
 */
class PickGoalPresenter @Inject constructor(private var view: PickGoalContract?,
                                            private val goalRepository: GoalRepository,
                                            private val preferenceDataStore: PreferenceDataStore,
                                            val disposables: CompositeDisposable) {

  fun saveGoal(day: Long, minutes: Int) {
    val goal = Goal(0, date = day, goal = minutes * 1000 * 60L)
    goalRepository.saveGoal(goal)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableCompletableObserver() {
              override fun onComplete() {
                preferenceDataStore.setUserHasOnboarded()
                view?.homeScreen()
              }

              override fun onError(e: Throwable) {
                view?.error()
              }

            })
  }

  fun unsubscribe() {
    disposables.dispose()
    view = null
  }
}