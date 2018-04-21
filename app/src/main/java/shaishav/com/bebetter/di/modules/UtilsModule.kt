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

package shaishav.com.bebetter.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.analytics.Analytics
import shaishav.com.bebetter.di.scopes.ApplicationScope
import shaishav.com.bebetter.utils.ResourceManager

/**
 * Created by shaishav.gandhi on 12/19/17.
 */
@Module
class UtilsModule {

  @Provides @ApplicationScope fun providesSchedulers(): Scheduler {
    return Schedulers.io()
  }

  @Provides fun providesDisposables(): CompositeDisposable {
    return CompositeDisposable()
  }

  @Provides @ApplicationScope fun providesResource(application: Application): ResourceManager {
    return ResourceManager(application.resources)
  }

  @Provides @ApplicationScope fun providesAnalytics(application: Application): Analytics {
    return Analytics(application)
  }


}