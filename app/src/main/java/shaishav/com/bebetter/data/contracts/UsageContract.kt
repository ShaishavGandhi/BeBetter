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
 * Created by shaishavgandhi05 on 10/23/16.
 */

object UsageContract {

    const val TABLE_USAGE = "usage"

    const val COLUMN_ID = "_id"
    const val COLUMN_DATE = "date"
    const val COLUMN_USAGE="usage"
    const val COLUMN_SERVER_ID = "server_id"

    const val CREATE_USAGE_TABLE = ("create table "
            + TABLE_USAGE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_DATE
            + " long not null,"+COLUMN_USAGE+" long not null, "+COLUMN_SERVER_ID+" text default 'NA');")
}
