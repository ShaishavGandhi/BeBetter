package shaishav.com.bebetter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import shaishav.com.bebetter.data.models.Stat;
import shaishav.com.bebetter.data.repository.StatsRepository;
import shaishav.com.bebetter.utils.BBApplication;
import shaishav.com.bebetter.utils.NotificationHelper;
import shaishav.com.bebetter.workflow.UsageWorkflow;

public class PhoneUnlockedReceiver extends BroadcastReceiver {

  @Inject UsageWorkflow workflow;
  @Inject NotificationHelper notificationHelper;
  @Inject StatsRepository statsRepository;

  @Override
  public void onReceive(Context context, Intent intent) {
    ((BBApplication) context.getApplicationContext())
            .addServiceComponent()
            .inject(this);

    if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

      workflow.phoneLocked(new Date().getTime());
      statsRepository.getStat().subscribeOn(Schedulers.io()).subscribeWith(new DisposableObserver<Stat>() {
        @Override
        public void onNext(Stat stat) {
          notificationHelper.updateNotification(notificationHelper.createNotification(stat.getUsage(), stat.getGoal()));
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
      });
    } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
      workflow.phoneUnlocked(new Date().getTime());
    }
  }

}
