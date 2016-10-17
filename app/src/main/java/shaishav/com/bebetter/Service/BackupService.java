package shaishav.com.bebetter.Service;

import android.app.IntentService;
import android.content.Intent;

import java.util.List;

import shaishav.com.bebetter.Data.models.Experience;
import shaishav.com.bebetter.Data.Source.ExperienceSource;
import shaishav.com.bebetter.Data.models.Usage;
import shaishav.com.bebetter.Data.Source.UsageSource;
import shaishav.com.bebetter.Network.NetworkRequests;

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
