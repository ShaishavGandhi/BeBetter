package shaishav.com.bebetter.di.modules

import dagger.Module
import dagger.Provides
import shaishav.com.bebetter.contracts.PickGoalContract
import shaishav.com.bebetter.di.scopes.ActivityScope

/**
 * Created by shaishav.gandhi on 3/1/18.
 */
@Module class PickGoalModule(val view: PickGoalContract) {

  @Provides @ActivityScope fun providesPickGoalModule(): PickGoalContract {
    return view
  }

}