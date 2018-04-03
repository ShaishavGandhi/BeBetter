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

package shaishav.com.bebetter.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import shaishav.com.bebetter.data.contracts.GoalContract
import shaishav.com.bebetter.data.contracts.PointContract
import shaishav.com.bebetter.data.contracts.UsageContract

/**
 * Created by shaishav.gandhi on 2/27/18.
 */
class MySQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

  override fun onCreate(database: SQLiteDatabase) {
    database.execSQL(UsageContract.CREATE_USAGE_TABLE)
    database.execSQL(GoalContract.CREATE_GOAL)
    database.execSQL(PointContract.CREATE_POINTS)
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

  }

  companion object {


    private val DATABASE_NAME = "bebetter.db"
    private val DATABASE_VERSION = 1
  }

}