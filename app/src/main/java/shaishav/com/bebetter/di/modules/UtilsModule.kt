package shaishav.com.bebetter.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.di.scopes.ApplicationScope
import shaishav.com.bebetter.utils.ResourceManager

/**
 * Created by shaishav.gandhi on 12/19/17.
 */
@Module
class UtilsModule {

  @Provides @ApplicationScope fun providesSchedulers(): Scheduler {
    return Schedulers.io()
  }

  @Provides fun providesDisposables(): CompositeDisposable {
    return CompositeDisposable()
  }

  @Provides @ApplicationScope fun providesResource(application: Application): ResourceManager {
    return ResourceManager(application.resources)
  }


}