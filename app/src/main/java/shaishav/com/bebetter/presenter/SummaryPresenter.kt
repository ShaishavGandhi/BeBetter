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
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.contracts.SummaryContract
import shaishav.com.bebetter.data.repository.SummaryRepository
import shaishav.com.bebetter.extensions.yesterday
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class SummaryPresenter @Inject constructor(
        var view: SummaryContract?,
        private val summaryRepository: SummaryRepository,
        val disposables: CompositeDisposable
) {

  fun start(date: Long) {
    val disposable = summaryRepository.summary(date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ summary ->
              view?.setSummary(summary)
            }, { error ->
              Timber.e(error)
            })

    disposables.add(disposable)
  }

  fun detach() {
    view = null
    disposables.dispose()
  }


}