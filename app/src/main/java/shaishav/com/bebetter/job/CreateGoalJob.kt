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

package shaishav.com.bebetter.job

import android.annotation.SuppressLint
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.data.repository.GoalRepository
import shaishav.com.bebetter.extensions.yesterday
import java.util.*
import java.util.concurrent.TimeUnit

class CreateGoalJob(val goalRepository: GoalRepository): Job() {

  companion object {
    const val TAG = "create_goal-job"

    fun scheduleJob() {
      // Run job every 2 hours or so. Consider increasing this to 4/6 maybe.
      JobRequest.Builder(CreateGoalJob.TAG)
              .setPeriodic(TimeUnit.HOURS.toMillis(2))
              .build()
              .schedule()
    }
  }

  @SuppressLint("CheckResult")
  override fun onRunJob(params: Params): Result {
    // Disposes off automatically when Completable succeeds/errors out.
    goalRepository.cloneGoal(Calendar.getInstance().yesterday().timeInMillis, System.currentTimeMillis())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {})
    return Result.SUCCESS
  }
}
