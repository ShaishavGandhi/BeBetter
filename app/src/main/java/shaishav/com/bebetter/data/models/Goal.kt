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

package shaishav.com.bebetter.data.models

import android.content.ContentValues
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import shaishav.com.bebetter.data.contracts.GoalContract

/**
 * Created by shaishav.gandhi on 1/5/18.
 */
@Parcelize data class Goal(val id: Long, val date: Long, val goal: Long): Parcelable {

  fun toContentValues(): ContentValues {
    val contentValues = ContentValues()
    contentValues.put(GoalContract.COLUMN_GOAL, goal)
    contentValues.put(GoalContract.COLUMN_DATE, date)
    return contentValues
  }

}