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

import com.uber.autodispose.LifecycleScopeProvider
import dagger.Module
import dagger.Provides
import shaishav.com.bebetter.contracts.SummaryContract
import shaishav.com.bebetter.di.scopes.ActivityScope

@Module class SummaryModule(val view: SummaryContract, val lifecycleProvider: LifecycleScopeProvider<*>) {

  @Provides @ActivityScope fun providesSummaryView(): SummaryContract {
    return view
  }

  @Provides @ActivityScope fun providesSummaryLifecycle(): LifecycleScopeProvider<*> {
    return lifecycleProvider
  }
}