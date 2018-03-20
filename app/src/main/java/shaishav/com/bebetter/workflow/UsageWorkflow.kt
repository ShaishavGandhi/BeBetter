package shaishav.com.bebetter.workflow

import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.data.repository.GoalRepository
import shaishav.com.bebetter.data.repository.UsageRepository
import java.util.*
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 1/10/18.
 */
class UsageWorkflow @Inject constructor(private val usageRepository: UsageRepository, private val goalRepository: GoalRepository) {

  /**
   * Workflow to go through when a phone is locked.
   * Stores the phone lock time and checks other conditions
   * to store session data
   *
   * @param lockTime
   */
  fun phoneLocked(lockTime: Long) {
    // Store phone locked time
    usageRepository.storePhoneLockedTime(lockTime)

    // Get the last unlocked time
    val unlockTime = usageRepository.lastUnlockedTime()

    when {
      unlockTime == 0L -> return
      hasDayChanged(lockTime, unlockTime) -> {
        // The day has passed between last lock and unlock

        // Copy over previous goal
        goalRepository.cloneGoal(lockTime).subscribe()

        // Construct the last minute of yesterday
        val previousDay = Calendar.getInstance()
        previousDay.timeInMillis = unlockTime
        previousDay.set(Calendar.HOUR_OF_DAY, 23)
        previousDay.set(Calendar.MINUTE, 59)

        // Get the last session time
        var currentSessionTime = previousDay.timeInMillis - unlockTime
        // Get the daily usage so far
        val dayUsage = usageRepository.rawDailyUsage()

        // Add it up to get final daily usage
        val usage = Usage(0, previousDay.timeInMillis, dayUsage + currentSessionTime)
        // Store in database
        usageRepository.insertSession(usage)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .subscribe()

        // TODO: Add points logic here
        addPoints()


        // Fast forward to the current date to calculate the session of the new day
        previousDay.set(Calendar.DAY_OF_YEAR, previousDay.get(Calendar.DAY_OF_YEAR) + 1)
        previousDay.set(Calendar.HOUR_OF_DAY, 0)
        previousDay.set(Calendar.MINUTE, 0)

        val lastUnlockTime = previousDay.timeInMillis

        // Calculate the new session
        currentSessionTime = lockTime - lastUnlockTime

        // Store phone unlock time
        usageRepository.storePhoneUnlockedTime(lastUnlockTime)
        // Store the session
        usageRepository.storeCurrentDayUsage(currentSessionTime)


      }
      else -> {
        // Still the same day. Just store the session time
        var sessionTime = lockTime - unlockTime
        // Get the daily usage so far and add to session
        val previousSession = usageRepository.rawDailyUsage()
        sessionTime += previousSession
        // Store in db
        usageRepository.storeCurrentDayUsage(sessionTime)
      }
    }

  }

  /**
   * Method to check whether the day has changed between the
   * last time phone was locked and unlocked
   *
   * @param lockTime phone lock time
   * @param unlockTime phone unlock time
   *
   * @return boolean
   */
  fun hasDayChanged(lockTime: Long, unlockTime: Long): Boolean {
    val lockDate = Calendar.getInstance()
    lockDate.timeInMillis = lockTime

    val unlockDate = Calendar.getInstance()
    unlockDate.timeInMillis = unlockTime

    return lockDate.get(Calendar.DAY_OF_MONTH) != unlockDate.get(Calendar.DAY_OF_MONTH)
  }

  /**
   * Workflow to go through when phone is unlocked.
   * Stores session timings and stores in database
   * if necessary
   *
   * @param unlockTime
   */
  fun phoneUnlocked(unlockTime: Long) {
    // Get the last unlocked time
    val lastUnlockedTime = usageRepository.lastUnlockedTime()

    if (hasDayChanged(lastUnlockedTime, unlockTime) && lastUnlockedTime != 0L) {
      // The day has changed. i.e User locked the phone before 12am, went
      // to sleep (presumably) and unlocked now. So we need to store
      // the previous session in database
      val currentSession = usageRepository.rawDailyUsage()
      val usage = Usage(0, lastUnlockedTime, currentSession)
      usageRepository.insertSession(usage)
              .subscribeOn(Schedulers.io())
              .subscribe()

      // Clone the goal
      goalRepository.cloneGoal(unlockTime).subscribe()

      // Reset session data to zero
      usageRepository.storeCurrentDayUsage(0)
    }

    // Store the unlock time
    usageRepository.storePhoneUnlockedTime(unlockTime)
  }

  /**
   * 
   */
  fun addPoints() {

  }
}