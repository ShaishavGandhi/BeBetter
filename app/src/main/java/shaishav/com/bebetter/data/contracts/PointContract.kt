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

package shaishav.com.bebetter.data.contracts;

/**
 * Created by shaishavgandhi05 on 11/6/16.
 */

object PointContract {

    const val TABLE_POINTS = "points"

    const val COLUMN_POINTS = "points"
    const val COLUMN_ID = "_id"
    const val COLUMN_DATE = "date"

    const val CREATE_POINTS = ("create table "
    + TABLE_POINTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_DATE
            + " long not null,"+COLUMN_POINTS+" long not null);")

}
