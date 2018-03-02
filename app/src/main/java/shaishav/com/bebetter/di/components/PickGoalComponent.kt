package shaishav.com.bebetter.di.components

import dagger.Module
import dagger.Subcomponent
import shaishav.com.bebetter.controller.PickGoalController
import shaishav.com.bebetter.di.modules.PickGoalModule
import shaishav.com.bebetter.di.scopes.ActivityScope

/**
 * Created by shaishav.gandhi on 3/1/18.
 */
@Subcomponent(modules = [(PickGoalModule::class)]) @ActivityScope interface PickGoalComponent {

  fun inject(controller: PickGoalController)

}