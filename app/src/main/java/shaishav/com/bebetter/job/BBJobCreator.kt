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

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator
import shaishav.com.bebetter.data.repository.GoalRepository

class BBJobCreator(val goalRepository: GoalRepository): JobCreator {

  override fun create(tag: String): Job? {
    when (tag) {
      CreateGoalJob.TAG ->
        return CreateGoalJob(goalRepository)
      RestartServiceJob.TAG ->
        return RestartServiceJob()
    }
    return null
  }
}
