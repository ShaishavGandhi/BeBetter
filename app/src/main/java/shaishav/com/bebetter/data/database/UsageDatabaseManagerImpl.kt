package shaishav.com.bebetter.data.database

import com.squareup.sqlbrite3.BriteContentResolver
import io.reactivex.Observable
import shaishav.com.bebetter.data.contracts.UsageContract
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.data.providers.UsageProvider

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
class UsageDatabaseManagerImpl(val contentResolver: BriteContentResolver): UsageDatabaseManager {
    override fun averageDailyUsage(): Observable<Long> {
        return contentResolver.createQuery(UsageProvider.CONTENT_URI,
                arrayOf("AVG(" + UsageContract.COLUMN_USAGE + ") "), null, null, null, false)
                .mapToOne {
                    return@mapToOne it.getInt(0).toLong()
                }
    }

    override fun usages(): Observable<List<Usage>> {
        return contentResolver
                .createQuery(UsageProvider.CONTENT_URI,
                        null,
                        null,
                        null,
                        UsageProvider.QUERY_SORT_ORDER,
                        false)
                .mapToList {
                    return@mapToList UsageProvider.cursorToUsage(it)
                }
    }

    override fun totalUsage(): Observable<Long> {
        return contentResolver.createQuery(UsageProvider.CONTENT_URI,
                arrayOf("SUM(" + UsageContract.COLUMN_USAGE + ") "), null, null, null, false)
                .mapToOne { return@mapToOne it.getInt(0).toLong() }
    }
}