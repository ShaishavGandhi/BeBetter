package shaishav.com.bebetter.di.components

import android.app.Application
import dagger.Component
import shaishav.com.bebetter.di.modules.AppModule
import shaishav.com.bebetter.di.modules.DatabaseModule
import shaishav.com.bebetter.di.modules.SummaryModule
import shaishav.com.bebetter.di.modules.UtilsModule
import shaishav.com.bebetter.di.scopes.ApplicationScope

/**
 * Created by shaishav.gandhi on 12/17/17.
 */
@ApplicationScope @Component(modules = [(AppModule::class), (DatabaseModule::class), (UtilsModule::class)])
interface AppComponent {

  fun getApplication(): Application
  fun addSummaryComponent(module: SummaryModule): SummaryComponent
  fun addServiceComponent(): ServiceComponent

}