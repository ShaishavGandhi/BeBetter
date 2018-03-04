package shaishav.com.bebetter.di

import shaishav.com.bebetter.contracts.PickGoalContract
import shaishav.com.bebetter.di.components.PickGoalComponent
import shaishav.com.bebetter.di.components.ServiceComponent
import shaishav.com.bebetter.di.components.SummaryComponent
import shaishav.com.bebetter.di.modules.PickGoalModule
import shaishav.com.bebetter.di.modules.SummaryModule

/**
 * Created by shaishav.gandhi on 12/17/17.
 */
interface DependencyGraph {

  fun addSummaryComponent(module: SummaryModule): SummaryComponent
  fun removeSummaryComponent()

  fun addPickGoalComponent(view: PickGoalContract): PickGoalComponent
  fun removePickGoalComponent()

  fun addServiceComponent(): ServiceComponent
  fun removeServiceComponent()
}