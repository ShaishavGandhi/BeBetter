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

  }

  override fun currentSession(currentTime: Long): Observable<Long> {
    return preferences
            .getLong(Constants.UNLOCKED)
            .asObservable()
            .map {
              // TODO: Replace with all the minutes and hours from preferences
              if (it == 0L) {
                return@map it
              }
              return@map (currentTime - it) / (1000 * 60)
            }
  }

  override fun dailyUsageSoFar(): Observable<Long> {
    return Observable.combineLatest(currentSession(Date().time),
            dailyUsage(), BiFunction { currentSession, dailyUsage ->
      return@BiFunction currentSession + dailyUsage
    })
  }

  override fun rawDailyUsage(): Long {
    return preferences.getLong(Constants.SESSION).get()
  }

  fun dailyUsage(): Observable<Long> {
    return preferences
            .getLong(Constants.SESSION)
            .asObservable()
            .map {
              return@map it / (1000 * 60)
            }
  }

  override fun insertPhoneLockTime(lockTime: Long) {
    editor.putLong(Constants.LOCKED, lockTime)
    editor.apply()
  }

  override fun insertPhoneUnlockTime(unlockTime: Long) {
    editor.putLong(Constants.UNLOCKED, unlockTime)
    editor.apply()
  }

  override fun lastUnlockTime(): Long {
    return preferences.getLong(Constants.UNLOCKED).get()
  }

  override fun lastLockTime(): Observable<Long> {
    return Observable.just(1)
  }

  override fun storeCurrentSessionTime(sessionTime: Long) {
    editor.putLong(Constants.SESSION, sessionTime)
    editor.apply()
  }
}