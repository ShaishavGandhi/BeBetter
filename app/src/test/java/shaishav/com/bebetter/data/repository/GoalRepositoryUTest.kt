package shaishav.com.bebetter.data.repository

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import shaishav.com.bebetter.data.database.GoalDatabaseManager
import shaishav.com.bebetter.data.models.Goal
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by shaishav.gandhi on 3/5/18.
 */
@RunWith(JUnit4::class)
class GoalRepositoryUTest {

  lateinit var goalRepository: GoalRepository
  @Mock lateinit var databaseManager: GoalDatabaseManager

  @Before @Throws fun setUp() {
    MockitoAnnotations.initMocks(this)
    goalRepository = GoalRepository(databaseManager)
  }

  @Test fun testCloneGoal() {
    val goal = Goal(id = 0, date = Date().time,goal = 220 * 1000 * 60)
    whenever(databaseManager.currentGoal()).thenReturn(Observable.just(goal))
    whenever(databaseManager.saveGoal(any())).thenReturn(Completable.complete())

    goalRepository.cloneGoal(Date().time)
            .test()
            .assertNoErrors()

    verify(databaseManager).saveGoal(any())
  }

}