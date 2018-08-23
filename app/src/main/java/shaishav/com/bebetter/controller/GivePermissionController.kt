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

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import shaishav.com.bebetter.R
import shaishav.com.bebetter.listener.ActivityInteractor
import shaishav.com.bebetter.utils.PermissionUtils.hasUsageStatsPermission

class GivePermissionController: BaseController() {

  lateinit var rootView: View
  lateinit var nextButton: Button
  lateinit var givePermissionButton: Button

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    rootView = inflater.inflate(R.layout.fragment_give_permission, container, false)
    initViews()
    initListeners()
    return rootView
  }

  private fun initListeners() {
    nextButton.setOnClickListener {
      if (!hasUsageStatsPermission(it.context)) {
        Toast.makeText(activity, R.string.need_usage_permission, Toast.LENGTH_LONG).show()
      }
    }

    givePermissionButton.setOnClickListener {
      val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
      startActivity(intent)
    }
  }

  override fun onAttach(view: View) {
    super.onAttach(view)
    if (hasUsageStatsPermission(view.context)) {
      router.setRoot(
              RouterTransaction.with(HomeController())
                      .pushChangeHandler(HorizontalChangeHandler())
                      .popChangeHandler(HorizontalChangeHandler()))
    }
  }

  private fun initViews() {
    nextButton = rootView.findViewById(R.id.nextButton)
    givePermissionButton = rootView.findViewById(R.id.givePermission)
  }

  override fun shouldShowBottomNav(): Boolean {
    return false
  }
}
