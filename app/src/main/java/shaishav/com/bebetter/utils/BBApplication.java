package shaishav.com.bebetter.utils;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;

import com.facebook.stetho.Stetho;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Calendar;

import shaishav.com.bebetter.di.DependencyGraph;
import shaishav.com.bebetter.di.components.AppComponent;
import shaishav.com.bebetter.di.components.DaggerAppComponent;
import shaishav.com.bebetter.di.components.SummaryComponent;
import shaishav.com.bebetter.di.modules.AppModule;
import shaishav.com.bebetter.di.modules.SummaryModule;
import shaishav.com.bebetter.service.BackgroundService;

/**
 * Created by Shaishav on 22-06-2016.
 */
public class BBApplication extends Application implements DependencyGraph {

    AppComponent appComponent;
    SummaryComponent summaryComponent;

    @Override public void onCreate(){
        super.onCreate();
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
        Stetho.initializeWithDefaults(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(getApplicationContext(), BackgroundService.class));
        } else {
            startService(new Intent(getApplicationContext(), BackgroundService.class));
        }

    }


    public void clearApplicationData() {

        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {

            String[] fileNames = applicationDirectory.list();

            for (String fileName : fileNames) {

                if (!fileName.equals("lib")) {

                    deleteFile(new File(applicationDirectory, fileName));

                }

            }

        }

        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();

    }

    public static boolean deleteFile(File file) {

        boolean deletedAll = true;

        if (file != null) {

            if (file.isDirectory()) {

                String[] children = file.list();

                for (int i = 0; i < children.length; i++) {

                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;

                }

            } else {

                deletedAll = file.delete();

            }

        }

        return deletedAll;

    }

    @NonNull @Override public SummaryComponent addSummaryComponent(@NotNull SummaryModule module) {
        if (summaryComponent == null) {
            summaryComponent = appComponent.addSummaryComponent(module);
        }
        return summaryComponent;
    }

    @Override
    public void removeSummaryComponent() {
        summaryComponent = null;
    }
}
