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

import android.view.View
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import shaishav.com.bebetter.listener.ActivityInteractor

abstract class BaseController: LifecycleController() {

  var interactor: ActivityInteractor? = null

  abstract fun shouldShowBottomNav(): Boolean

  override fun onAttach(view: View) {
    super.onAttach(view)
    if (activity is ActivityInteractor) {
      interactor = (activity as ActivityInteractor)
      interactor?.showBottomNav(shouldShowBottomNav())
    }
  }
}
