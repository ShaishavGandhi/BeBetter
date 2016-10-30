package shaishav.com.bebetter.service;

import android.app.IntentService;
import android.content.Intent;

import java.util.List;

import shaishav.com.bebetter.data.models.Experience;
import shaishav.com.bebetter.data.source.ExperienceSource;
import shaishav.com.bebetter.data.models.Usage;
import shaishav.com.bebetter.data.source.UsageSource;
import shaishav.com.bebetter.network.NetworkRequests;

/**
 * Created by Shaishav on 25-07-2016.
 */
public class BackupService extends IntentService {

    ExperienceSource experienceSource;
    UsageSource usageSource;
    List<Experience> experiences;
    List<Usage> usages;

    public BackupService(){
        super("BackupService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        experienceSource = new ExperienceSource(getApplicationContext());
        experienceSource.open();
        experiences = experienceSource.getLessonsForBackup();
        experienceSource.close();

        usageSource = new UsageSource(getApplicationContext());
        usageSource.open();
        usages = usageSource.getUsagesForBackup();
        usageSource.close();

        NetworkRequests networkRequests = NetworkRequests.getInstance(getApplicationContext());

        if(networkRequests.isNetworkAvailable()) {
            networkRequests.syncLesson(experiences);
            networkRequests.syncUsage(usages);
        }

    }
}
