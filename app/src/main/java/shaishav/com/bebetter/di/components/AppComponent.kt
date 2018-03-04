package shaishav.com.bebetter.di.components

import android.app.Application
import dagger.Component
import shaishav.com.bebetter.activities.MainActivity
import shaishav.com.bebetter.di.modules.*
import shaishav.com.bebetter.di.scopes.ApplicationScope

/**
 * Created by shaishav.gandhi on 12/17/17.
 */
@ApplicationScope @Component(modules = [(AppModule::class), (DatabaseModule::class), (UtilsModule::class)])
interface AppComponent {

  fun getApplication(): Application
  fun addSummaryComponent(module: SummaryModule): SummaryComponent
  fun addServiceComponent(): ServiceComponent
  fun addPickGoalComponent(module: PickGoalModule): PickGoalComponent

  fun inject(activity: MainActivity)

}