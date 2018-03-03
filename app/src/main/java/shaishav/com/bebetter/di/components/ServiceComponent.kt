package shaishav.com.bebetter.di.components

import dagger.Subcomponent
import shaishav.com.bebetter.di.scopes.ActivityScope
import shaishav.com.bebetter.di.scopes.ApplicationScope
import shaishav.com.bebetter.receiver.PhoneUnlockedReceiver
import shaishav.com.bebetter.service.UsageService

/**
 * Created by shaishav.gandhi on 2/25/18.
 */
@ActivityScope @Subcomponent interface ServiceComponent {

  fun inject(receiver: PhoneUnlockedReceiver)
  fun inject(service: UsageService)

}