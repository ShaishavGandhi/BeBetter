package shaishav.com.bebetter.data.preferences

import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
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
            }
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
}