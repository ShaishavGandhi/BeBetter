package shaishav.com.bebetter.di.components

import dagger.Subcomponent
import shaishav.com.bebetter.di.modules.SummaryModule
import shaishav.com.bebetter.di.scopes.ActivityScope
import shaishav.com.bebetter.fragments.SummaryFragment
import shaishav.com.bebetter.fragments.SummaryFragment2

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
@Subcomponent(modules = [(SummaryModule::class)]) @ActivityScope interface SummaryComponent {

    fun inject(fragment: SummaryFragment2)
}