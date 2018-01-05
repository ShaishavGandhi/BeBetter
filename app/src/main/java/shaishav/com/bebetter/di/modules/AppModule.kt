package shaishav.com.bebetter.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import shaishav.com.bebetter.di.scopes.ApplicationScope
import shaishav.com.bebetter.utils.BBApplication

/**
 * Created by shaishav.gandhi on 12/17/17.
 */
@Module class AppModule(val app : BBApplication) {

    @Provides @ApplicationScope fun getApplication() : Application {
        return app
    }

}