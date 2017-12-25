package shaishav.com.bebetter.contracts

/**
 * Created by shaishav.gandhi on 12/17/17.
 */
interface SummaryContract {
    
    fun setAverageDaiyUsage(usage: Long)
    fun setDailyUsage(usage: Long)
    fun setCurrentSession(usage: Long)

}