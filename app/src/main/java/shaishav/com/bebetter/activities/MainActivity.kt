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

package shaishav.com.bebetter.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bluelinelabs.conductor.*
import shaishav.com.bebetter.R
import shaishav.com.bebetter.controller.PickGoalController
import shaishav.com.bebetter.controller.HomeController
import shaishav.com.bebetter.controller.SummaryController
import shaishav.com.bebetter.data.preferences.PreferenceDataStore
import shaishav.com.bebetter.di.DependencyGraph
import shaishav.com.bebetter.extensions.yesterday
import shaishav.com.bebetter.utils.BBApplication
import shaishav.com.bebetter.utils.Constants
import shaishav.com.bebetter.utils.NotificationHelper
import shaishav.com.bebetter.utils.getScreenName
import java.util.*
import javax.inject.Inject
import android.app.AppOpsManager
import android.content.Context
import android.content.Context.APP_OPS_SERVICE
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import shaishav.com.bebetter.controller.GivePermissionController
import shaishav.com.bebetter.utils.PermissionUtils.hasUsageStatsPermission


/**
 * Created by shaishav.gandhi on 3/3/18.
 */
class MainActivity : AppCompatActivity() {


  private lateinit var router: Router
  private lateinit var toolbar: Toolbar
  @Inject lateinit var preferenceDataStore: PreferenceDataStore

  private val rootController: Controller
    get() = if (preferenceDataStore.hasUserOnBoarded()) {
      HomeController()
    } else PickGoalController()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    if (application is DependencyGraph) {
      (application as BBApplication).appComponent.inject(this)
    }
    toolbar = findViewById(R.id.toolbar)
    setSupportActionBar(toolbar)

    val container = findViewById<FrameLayout>(R.id.container_body)

    router = Conductor.attachRouter(this, container, savedInstanceState)
    router.addChangeListener(object : ControllerChangeHandler.ControllerChangeListener {
      override fun onChangeStarted(to: Controller?, from: Controller?, isPush: Boolean, container: ViewGroup, handler: ControllerChangeHandler) {
      }

      override fun onChangeCompleted(to: Controller?, from: Controller?, isPush: Boolean, container: ViewGroup, handler: ControllerChangeHandler) {
        showBackButtonIfNecessary()
      }
    })

    val bundle = intent.extras
    if (bundle?.getScreenName() != null) {
      summaryScreen(bundle.getScreenName()!!)
      return
    }

    val rootController = rootController
    if (!router.hasRootController()) {
      router.setRoot(RouterTransaction.with(rootController))
    }

    if (!hasUsageStatsPermission(this)) {
      router.pushController(RouterTransaction.with(GivePermissionController())
              .pushChangeHandler(HorizontalChangeHandler())
              .popChangeHandler(HorizontalChangeHandler()))
    }
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    intent?.let {
      val bundle = it.extras
      if (bundle?.getScreenName() != null) {
        summaryScreen(bundle.getScreenName()!!)
      }
    }
  }

  fun summaryScreen(name: String) {
    router.setRoot(RouterTransaction.with(rootController))
    router.pushController(RouterTransaction.with(getController(name)))
    intent.removeExtra("screen_name")
  }

  private fun getController(screenName: String): Controller {
    if (screenName == SummaryController.KEY) {
      return SummaryController(Calendar.getInstance().yesterday().timeInMillis)
    }
    return HomeController()
  }

  fun showBackButtonIfNecessary() {
    if (router.backstackSize > 1) {
      supportActionBar?.setDisplayHomeAsUpEnabled(true)
      supportActionBar?.setDisplayShowHomeEnabled(true)
    } else {
      supportActionBar?.setDisplayHomeAsUpEnabled(false)
      supportActionBar?.setDisplayShowHomeEnabled(false)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    return true
  }

  override fun onBackPressed() {
    if (!router.handleBack()) {
      super.onBackPressed()
    }
    showBackButtonIfNecessary()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    val id = item.itemId

    if (id == android.R.id.home) {
      onBackPressed()
      return true
    }

    return if (id == R.id.action_settings) {
      true
    } else super.onOptionsItemSelected(item)
  }

  fun setToolbarTitle(title: String) {
    supportActionBar?.title = title
  }

}