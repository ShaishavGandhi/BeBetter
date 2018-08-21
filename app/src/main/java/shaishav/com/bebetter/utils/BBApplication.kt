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

package shaishav.com.bebetter.utils

import android.app.Application
import android.content.Intent
import android.os.Build
import com.crashlytics.android.Crashlytics
import com.evernote.android.job.JobManager
import com.facebook.stetho.Stetho
import com.uber.autodispose.LifecycleScopeProvider
import io.fabric.sdk.android.Fabric
import shaishav.com.bebetter.BuildConfig
import shaishav.com.bebetter.contracts.PickGoalContract
import shaishav.com.bebetter.contracts.SummaryContract
import shaishav.com.bebetter.data.repository.GoalRepository
import shaishav.com.bebetter.di.DependencyGraph
import shaishav.com.bebetter.di.components.*
import shaishav.com.bebetter.di.modules.AppModule
import shaishav.com.bebetter.di.modules.PickGoalModule
import shaishav.com.bebetter.di.modules.HomeModule
import shaishav.com.bebetter.di.modules.SummaryModule
import shaishav.com.bebetter.job.BBJobCreator
import shaishav.com.bebetter.job.CreateGoalJob
import shaishav.com.bebetter.job.RestartServiceJob
import shaishav.com.bebetter.logging.ReleaseTree
import shaishav.com.bebetter.service.ServiceRestarter
import shaishav.com.bebetter.service.UsageService
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 2/23/18.
 */
class BBApplication : Application(), DependencyGraph {

  lateinit var appComponent: AppComponent
  private var homeComponent: HomeComponent? = null
  private var serviceComponent: ServiceComponent? = null
  private var pickGoalComponent: PickGoalComponent? = null
  private var summaryComponent: SummaryComponent? = null

  @Inject lateinit var goalRepository: GoalRepository

  override fun onCreate() {
    super.onCreate()
    Thread.setDefaultUncaughtExceptionHandler(ServiceRestarter(this))
    appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()

    // Probably not the best way to do this, but will figure out later.
    appComponent.inject(this)

    JobManager.create(this).addJobCreator(BBJobCreator(goalRepository))
    CreateGoalJob.scheduleJob()
    RestartServiceJob.scheduleJob()

    Stetho.initializeWithDefaults(this)
    initCrashlytics()

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }

    Timber.plant(ReleaseTree())


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      startForegroundService(Intent(applicationContext, UsageService::class.java))
    } else {
      startService(Intent(applicationContext, UsageService::class.java))
    }

  }

  fun initCrashlytics() {
    Fabric.with(this, Crashlytics())
  }

  override fun addHomeComponent(module: HomeModule): HomeComponent {
    if (homeComponent == null) {
      homeComponent = appComponent.addHomeComponent(module)
    }
    return homeComponent as HomeComponent
  }

  override fun removeHomeComponent() {
    homeComponent = null
  }

  override fun addServiceComponent(): ServiceComponent {
    if (serviceComponent == null) {
      serviceComponent = appComponent.addServiceComponent()
    }
    return serviceComponent as ServiceComponent
  }

  override fun addPickGoalComponent(view: PickGoalContract, lifecycleScopeProvider: LifecycleScopeProvider<*>): PickGoalComponent {
    if (pickGoalComponent == null) {
      pickGoalComponent = appComponent.addPickGoalComponent(PickGoalModule(view, lifecycleScopeProvider))
    }
    return pickGoalComponent as PickGoalComponent
  }

  override fun removePickGoalComponent() {
    pickGoalComponent = null
  }

  override fun removeServiceComponent() {
    serviceComponent = null
  }

  override fun addSummaryComponent(view: SummaryContract, lifecycleScopeProvider: LifecycleScopeProvider<*>): SummaryComponent {
    if (summaryComponent == null) {
      summaryComponent = appComponent.addSummaryComponent(SummaryModule(view, lifecycleScopeProvider))
    }
    return summaryComponent!!
  }

  override fun removeSummaryComponent() {
    summaryComponent = null
  }
}