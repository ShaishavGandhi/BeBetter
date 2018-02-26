package shaishav.com.bebetter.receiver;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Date;

import javax.inject.Inject;

import shaishav.com.bebetter.data.repository.GoalRepository;
import shaishav.com.bebetter.data.repository.UsageRepository;
import shaishav.com.bebetter.data.source.PreferenceSource;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.utils.BBApplication;
import shaishav.com.bebetter.utils.NotificationHelper;
import shaishav.com.bebetter.utils.TimeWidget;
import shaishav.com.bebetter.workflow.UsageWorkflow;

public class PhoneUnlockedReceiver extends BroadcastReceiver {

  @Inject UsageWorkflow workflow;

  @Override
  public void onReceive(Context context, Intent intent) {
    ((BBApplication) context.getApplicationContext())
            .addServiceComponent()
            .inject(this);

    PreferenceSource preferenceSource = PreferenceSource.getInstance(context);


    if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

      workflow.phoneLocked(new Date().getTime());

      NotificationHelper notif = new NotificationHelper(context.getApplicationContext());
      notif.updateNotification(notif.createNotification(preferenceSource.getSessionTime() / (preferenceSource.getUsageUnit()), preferenceSource.getGoal() / (preferenceSource.getUsageUnit())));


    } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

      workflow.phoneUnlocked(new Date().getTime());

      //Update screen widget if present

      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.time_widget);
      ComponentName thisWidget = new ComponentName(context, TimeWidget.class);
      AppWidgetManager.getInstance(context).updateAppWidget(thisWidget, views);

      updateWidget(context);

    }
  }

  void updateWidget(Context context) {
    Intent intent = new Intent(context, TimeWidget.class);
    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
    // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
    // since it seems the onUpdate() is only fired on that:
    int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, TimeWidget.class));
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
    context.sendBroadcast(intent);
  }
}
