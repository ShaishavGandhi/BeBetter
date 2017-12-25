package shaishav.com.bebetter.di.modules

import dagger.Module
import dagger.Provides
import shaishav.com.bebetter.contracts.SummaryContract
import shaishav.com.bebetter.di.scopes.ActivityScope

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
@Module class SummaryModule(val view: SummaryContract) {

    @Provides @ActivityScope fun providesView(): SummaryContract {
        return view
    }
}