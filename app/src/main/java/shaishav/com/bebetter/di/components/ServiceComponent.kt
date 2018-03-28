package shaishav.com.bebetter.di.components

import dagger.Subcomponent
import shaishav.com.bebetter.di.scopes.ActivityScope
import shaishav.com.bebetter.service.UsageService
import shaishav.com.bebetter.service.WorkflowService

/**
 * Created by shaishav.gandhi on 2/25/18.
 */
@ActivityScope @Subcomponent interface ServiceComponent {

  fun inject(service: UsageService)
  fun inject(service: WorkflowService)

}