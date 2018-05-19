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

package shaishav.com.bebetter.data.preferences

import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.utils.Constants
import java.util.*

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
class PreferenceDataStoreImpl(val preferences: RxSharedPreferences, val editor: SharedPreferences.Editor) : PreferenceDataStore {

  companion object {
    val KEY_CURRENT_DAILY_SESSION = "session"
    val KEY_LOCKED = "locked"
    val KEY_UNLOCKED = "unlocked"
    val USER_ONBOARDED = "userOnboarded"
    val UNLOCK_COUNTER = "unlockCounter"
  }

  override fun currentSession(currentTime: Long): Observable<Long> {
    return preferences
            .getLong(KEY_UNLOCKED)
            .asObservable()
            .map {
              // TODO: Replace with all the minutes and hours from preferences
              if (it == 0L) {
                return@map it
              }
              return@map (Date().time - it) / (1000 * 60)
            }.subscribeOn(Schedulers.io())
  }

  override fun dailyUsageSoFar(): Observable<Long> {
    return Observable.combineLatest(currentSession(Date().time),
            dailyUsage(), BiFunction { currentSession, dailyUsage ->
      return@BiFunction currentSession + dailyUsage
    })
  }

  override fun rawDailyUsage(): Long {
    return preferences.getLong(KEY_CURRENT_DAILY_SESSION).get()
  }

  fun dailyUsage(): Observable<Long> {
    return preferences
            .getLong(KEY_CURRENT_DAILY_SESSION)
            .asObservable()
            .subscribeOn(Schedulers.io())
            .map {
              return@map it / (1000 * 60)
            }
  }

  override fun insertPhoneLockTime(lockTime: Long) {
    editor.putLong(KEY_LOCKED, lockTime)
    editor.apply()
  }

  override fun insertPhoneUnlockTime(unlockTime: Long) {
    editor.putLong(KEY_UNLOCKED, unlockTime)
    editor.apply()
  }

  override fun lastUnlockTime(): Long {
    return preferences.getLong(KEY_UNLOCKED).get()
  }

  override fun lastLockTime(): Observable<Long> {
    return Observable.just(1)
  }

  override fun storeCurrentDayUsage(sessionTime: Long) {
    editor.putLong(KEY_CURRENT_DAILY_SESSION, sessionTime)
    editor.apply()
  }

  override fun hasUserOnBoarded(): Boolean {
    return preferences.getBoolean(USER_ONBOARDED).get()
  }

  override fun setUserHasOnboarded() {
    editor.putBoolean(USER_ONBOARDED, true).apply()
  }

  override fun incrementUnlockCounter() {
    var counter = preferences.getInteger(UNLOCK_COUNTER, 0).get()
    counter = counter.inc()
    editor.putInt(UNLOCK_COUNTER, counter).apply()
  }

  override fun resetUnlockCounter() {
    editor.putInt(UNLOCK_COUNTER, 0).apply()
  }

  override fun totalUnlocks(): Observable<Int> {
    return preferences.getInteger(UNLOCK_COUNTER, 0).asObservable()
  }
}