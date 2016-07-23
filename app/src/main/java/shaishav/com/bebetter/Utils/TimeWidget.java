package shaishav.com.bebetter.Utils;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.util.Date;

import shaishav.com.bebetter.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Implementation of App Widget functionality.
 */
public class TimeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.time_widget);
        String daily_session = getData(context);
        views.setTextViewText(R.id.daily_usage, daily_session);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static String getData(Context context){
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE);
        String current_session = String.valueOf((new Date().getTime() - preferences.getLong(Constants.UNLOCKED, 0)) / (1000 * 60));
        String daily_session = String.valueOf((preferences.getLong(Constants.SESSION, 0)) / (1000 * 60));
        daily_session = String.valueOf(Long.parseLong(daily_session) + Long.parseLong(current_session));
        return daily_session;
    }
}

