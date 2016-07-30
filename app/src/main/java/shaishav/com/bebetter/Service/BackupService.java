package shaishav.com.bebetter.Service;

import android.app.IntentService;
import android.content.Intent;

import java.util.List;

import shaishav.com.bebetter.Data.Lesson;
import shaishav.com.bebetter.Data.LessonSource;
import shaishav.com.bebetter.Data.Usage;
import shaishav.com.bebetter.Data.UsageSource;
import shaishav.com.bebetter.Utils.SyncRequests;

/**
 * Created by Shaishav on 25-07-2016.
 */
public class BackupService extends IntentService {

    LessonSource lessonSource;
    UsageSource usageSource;
    List<Lesson> lessons;
    List<Usage> usages;

    public BackupService(){
        super("BackupService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        lessonSource = new LessonSource(getApplicationContext());
        lessonSource.open();
        lessons = lessonSource.getLessonsForBackup();
        lessonSource.close();

        usageSource = new UsageSource(getApplicationContext());
        usageSource.open();
        usages = usageSource.getUsagesForBackup();
        usageSource.close();

        SyncRequests syncRequests = SyncRequests.getInstance(getApplicationContext());
        syncRequests.syncLesson(lessons);
        syncRequests.syncUsage(usages);

    }
}
