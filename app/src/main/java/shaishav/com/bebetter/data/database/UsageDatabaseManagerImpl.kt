package shaishav.com.bebetter.data.database

import android.content.ContentValues
import android.support.annotation.WorkerThread
import com.squareup.sqlbrite2.BriteContentResolver
import com.squareup.sqlbrite2.BriteDatabase
import io.reactivex.Observable
import shaishav.com.bebetter.data.contracts.UsageContract
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.data.providers.UsageProvider
import shaishav.com.bebetter.di.scopes.ApplicationScope
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
@ApplicationScope class UsageDatabaseManagerImpl @Inject constructor(
        val contentResolver: BriteContentResolver, val database: BriteDatabase
) : UsageDatabaseManager {

  override fun averageDailyUsage(): Observable<Long> {
    return contentResolver.createQuery(UsageProvider.CONTENT_URI,
            arrayOf("AVG(" + UsageContract.COLUMN_USAGE + ") "), null, null, null, false)
            .mapToOne {
              // TODO : Replace with preference
              return@mapToOne it.getInt(0).toLong() / (1000 * 60)
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
            .mapToOne {
              // TODO : Replace with preference instead of static 1000 * 60
              return@mapToOne it.getInt(0).toLong() / (1000 * 60)
            }
  }

  @WorkerThread override fun insertSession(usage: Usage): Long {
    val contentValues = ContentValues()
    contentValues.put(UsageContract.COLUMN_DATE, usage.date)
    contentValues.put(UsageContract.COLUMN_USAGE, usage.usage)

    return database.insert(UsageContract.TABLE_USAGE, contentValues)
  }
}