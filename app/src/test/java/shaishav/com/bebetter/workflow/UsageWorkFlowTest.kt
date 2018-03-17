package shaishav.com.bebetter.workflow

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import shaishav.com.bebetter.data.repository.GoalRepository
import shaishav.com.bebetter.data.repository.UsageRepository
import java.util.*

/**
 * Created by shaishav.gandhi on 3/12/18.
 */
@RunWith(JUnit4::class)
class UsageWorkFlowTest {

  @Mock lateinit var usageRepository: UsageRepository
  @Mock lateinit var goalRepository: GoalRepository
  lateinit var workflow: UsageWorkflow

  @Before @Throws fun setup() {
    MockitoAnnotations.initMocks(this)
    workflow = UsageWorkflow(usageRepository, goalRepository)
  }

  @Test fun testHasDayChangedWithChangedDate() {
    val currentDate = Calendar.getInstance()
    val currentTime = currentDate.timeInMillis
    currentDate.set(Calendar.DAY_OF_YEAR, currentDate.get(Calendar.DAY_OF_YEAR) + 1)
    val tomorrowTime = currentDate.timeInMillis

    assertTrue(workflow.hasDayChanged(currentTime, tomorrowTime))
  }

  @Test fun testHasDayChangedWithUnchangedDate() {
    val currentDate = Calendar.getInstance()
    val currentTime = currentDate.timeInMillis
    currentDate.set(Calendar.HOUR_OF_DAY, currentDate.get(Calendar.HOUR_OF_DAY) + 1)
    val tomorrowTime = currentDate.timeInMillis

    assertFalse(workflow.hasDayChanged(currentTime, tomorrowTime))
  }
}