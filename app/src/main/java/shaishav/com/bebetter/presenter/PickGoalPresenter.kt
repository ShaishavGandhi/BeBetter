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