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

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import shaishav.com.bebetter.R
import shaishav.com.bebetter.controller.PickGoalController
import shaishav.com.bebetter.controller.SummaryController
import shaishav.com.bebetter.data.preferences.PreferenceDataStore
import shaishav.com.bebetter.di.DependencyGraph
import shaishav.com.bebetter.utils.BBApplication
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 3/3/18.
 */
class MainActivity : AppCompatActivity() {


  private lateinit var router: Router
  @Inject lateinit var preferenceDataStore: PreferenceDataStore

  private val rootController: Controller
    get() = if (preferenceDataStore.hasUserOnBoarded()) {
      SummaryController()
    } else PickGoalController()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    if (application is DependencyGraph) {
      (application as BBApplication).appComponent.inject(this)
    }
    val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
    setSupportActionBar(toolbar)

    val container = findViewById<FrameLayout>(R.id.container_body)

    router = Conductor.attachRouter(this, container, savedInstanceState)
    val rootController = rootController
    if (!router.hasRootController()) {
      router.setRoot(RouterTransaction.with(rootController))
    }
  }

  override fun onResume() {
    super.onResume()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    return true
  }

  override fun onBackPressed() {
    if (!router.handleBack()) {
      super.onBackPressed()
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    val id = item.itemId


    return if (id == R.id.action_settings) {
      true
    } else super.onOptionsItemSelected(item)
  }

}