package shaishav.com.bebetter.workflow

import io.reactivex.Observable
import shaishav.com.bebetter.data.repository.UsageRepository
import java.util.*

/**
 * Created by shaishav.gandhi on 1/10/18.
 */
class UsageWorkflow(val usageRepository: UsageRepository) {


    fun phoneLocked(lockTime: Long) {
        usageRepository.storePhoneLockedTime(lockTime)

        val unlockTime = usageRepository.lastUnlockedTime()

        if (unlockTime == 0L) {
            return
        } else if (hasDayChanged(lockTime, unlockTime)){
            val previousDay = Calendar.getInstance()
            previousDay.timeInMillis = unlockTime
            previousDay.set(Calendar.HOUR_OF_DAY, 23)
            previousDay.set(Calendar.MINUTE, 59)

            var currentSessionTime = previousDay.timeInMillis - unlockTime
            var session = usageRepository
            usageRepository.storeDailySession(previousDay.timeInMillis, )

        } else {
            // Still the same day. Just store the session time
            var sessionTime = lockTime - unlockTime
            // TODO: Get previous session
            val previousSession = 0L
            sessionTime += previousSession
            usageRepository.storeCurrentSession(sessionTime)
        }

    }

    private fun hasDayChanged(lockTime: Long, unlockTime: Long): Boolean {
        val lockDate = Calendar.getInstance()
        lockDate.timeInMillis = lockTime

        val unlockDate = Calendar.getInstance()
        unlockDate.timeInMillis = unlockTime

        return lockDate.get(Calendar.DAY_OF_MONTH) != unlockDate.get(Calendar.DAY_OF_MONTH)
    }

    fun phoneUnlocked(unlockTime: Long) {
    }
}