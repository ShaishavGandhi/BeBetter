/*
 *  Copyright (c) 2018 Shaishav Gandhi
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
 *
 */

package shaishav.com.bebetter.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import shaishav.com.bebetter.R
import shaishav.com.bebetter.data.repository.UsageRepository
import shaishav.com.bebetter.extensions.toFormattedTime
import shaishav.com.bebetter.utils.BBApplication
import javax.inject.Inject
import android.app.PendingIntent
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import android.content.Intent
import shaishav.com.bebetter.activities.MainActivity


class DailyUsageWidget : AppWidgetProvider() {

  @Inject lateinit var usageRepository: UsageRepository

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    // There may be multiple widgets active, so update all of them
    (context.applicationContext as BBApplication).appComponent.addServiceComponent().inject(this)
    for (appWidgetId in appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId)
    }
  }

  override fun onEnabled(context: Context) {
    // Enter relevant functionality for when the first widget is created
  }

  override fun onDisabled(context: Context) {
    // Enter relevant functionality for when the last widget is disabled
  }


  internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                               appWidgetId: Int) {

    val usage = usageRepository.rawDailyUsage().toFormattedTime()
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.daily_usage)
    views.setTextViewText(R.id.usage, usage)

    val intent = Intent(context, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
    views.setOnClickPendingIntent(R.id.root, pendingIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
  }
}
