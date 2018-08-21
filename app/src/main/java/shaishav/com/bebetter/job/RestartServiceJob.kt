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

import android.content.Intent
import android.os.Build
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import shaishav.com.bebetter.service.UsageService
import java.util.concurrent.TimeUnit

class RestartServiceJob: Job() {

  companion object {
    const val TAG = "restart-service"

    fun scheduleJob() {
      // Run job every 2 hours or so. Consider increasing this to 4/6 maybe.
      JobRequest.Builder(CreateGoalJob.TAG)
              .setPeriodic(TimeUnit.HOURS.toMillis(3))
              .build()
              .schedule()
    }
  }

  override fun onRunJob(params: Params): Result {
    context.stopService(Intent(context, UsageService::class.java))
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      context.startForegroundService(Intent(context, UsageService::class.java))
    } else {
      context.startService(Intent(context, UsageService::class.java))
    }
    return Result.SUCCESS
  }
}
