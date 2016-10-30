package shaishav.com.bebetter.receiver;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import shaishav.com.bebetter.data.source.PreferenceSource;
import shaishav.com.bebetter.R;
import shaishav.com.bebetter.utils.Notification;
import shaishav.com.bebetter.utils.TimeWidget;

public class PhoneUnlockedReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

        PreferenceSource preferenceSource = PreferenceSource.getInstance(context);


        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

            preferenceSource.savePhoneLockTime();

            Notification notif = new Notification();
            notif.updateNotification(context,
                    notif.createNotification(context,String.valueOf(preferenceSource.getSessionTime()/(preferenceSource.getUsageUnit())),
                            String.valueOf(preferenceSource.getGoal()/preferenceSource.getUsageUnit())));


        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            preferenceSource.savePhoneUnlockTime();

            //Update screen widget if present

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.time_widget);
            ComponentName thisWidget = new ComponentName(context, TimeWidget.class);
            AppWidgetManager.getInstance( context ).updateAppWidget( thisWidget, views );

            updateWidget(context);

        }
    }

    void updateWidget(Context context){
        Intent intent = new Intent(context,TimeWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, TimeWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        context.sendBroadcast(intent);
    }
}
