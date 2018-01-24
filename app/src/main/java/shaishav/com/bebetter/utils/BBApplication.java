package shaishav.com.bebetter.utils;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

import com.facebook.stetho.Stetho;

import org.jetbrains.annotations.NotNull;

import shaishav.com.bebetter.BuildConfig;
import shaishav.com.bebetter.di.DependencyGraph;
import shaishav.com.bebetter.di.components.AppComponent;
import shaishav.com.bebetter.di.components.DaggerAppComponent;
import shaishav.com.bebetter.di.components.SummaryComponent;
import shaishav.com.bebetter.di.modules.AppModule;
import shaishav.com.bebetter.di.modules.SummaryModule;
import shaishav.com.bebetter.service.UsageService;
import timber.log.Timber;

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

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(getApplicationContext(), UsageService.class));
        } else {
            startService(new Intent(getApplicationContext(), UsageService.class));
        }

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
