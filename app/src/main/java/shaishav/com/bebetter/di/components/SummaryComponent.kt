package shaishav.com.bebetter.di.components

import dagger.Subcomponent
import shaishav.com.bebetter.di.modules.SummaryModule
import shaishav.com.bebetter.di.scopes.ActivityScope
import shaishav.com.bebetter.fragments.SummaryController

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
@Subcomponent(modules = [(SummaryModule::class)]) @ActivityScope interface SummaryComponent {

    fun inject(controller: SummaryController)
}