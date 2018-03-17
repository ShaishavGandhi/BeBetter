package shaishav.com.bebetter.data.repository

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import shaishav.com.bebetter.data.models.Usage
import java.util.*

/**
 * Created by shaishav.gandhi on 3/17/18.
 */
@RunWith(JUnit4::class)
class StreakRepositoryUTest {

  lateinit var streakRepository: StreakRepository
  @Mock lateinit var usageRepository: UsageRepository
  @Mock lateinit var goalRepository: GoalRepository

  @Before @Throws fun setUp() {
    MockitoAnnotations.initMocks(this)
    streakRepository = StreakRepository(goalRepository, usageRepository)
  }

  @Test fun testCurrentStreak_noData() {
    val goals = GoalSampleData.getSampleGoals(1)
    whenever(goalRepository.goals()).thenReturn(Observable.just(goals))
    whenever(usageRepository.usages()).thenReturn(Observable.just(emptyList()))

    val testObserver = streakRepository.currentStreak().test()

    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)
    assertEquals(0, testObserver.values()[0])
  }

  @Test fun testCurrentStreak_oneStreak() {
    val goals = GoalSampleData.getSampleGoals(2)
    whenever(goalRepository.goals()).thenReturn(Observable.just(goals))

    val calendar = Calendar.getInstance()

    val usage = Usage(id = 0, date = calendar.timeInMillis, usage = 180 * 1000 * 60L)
    val usages = listOf(usage)
    whenever(usageRepository.usages()).thenReturn(Observable.just(usages))

    val testObserver = streakRepository.currentStreak().test()

    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)
    assertEquals(1, testObserver.values()[0])
  }

  @Test fun testCurrentStreak_zeroStreak() {
    val goals = GoalSampleData.getSampleGoals(2)
    whenever(goalRepository.goals()).thenReturn(Observable.just(goals))

    val calendar = Calendar.getInstance()

    val usage = Usage(id = 0, date = calendar.timeInMillis, usage = 280 * 1000 * 60L)
    val usages = listOf(usage)
    whenever(usageRepository.usages()).thenReturn(Observable.just(usages))

    val testObserver = streakRepository.currentStreak().test()

    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)
    assertEquals(0, testObserver.values()[0])
  }

  @Test fun testCurrentStreak_twoStreak() {
    val goals = GoalSampleData.getSampleGoals(2)
    whenever(goalRepository.goals()).thenReturn(Observable.just(goals))

    val calendar = Calendar.getInstance()

    val usage = Usage(id = 0, date = calendar.timeInMillis, usage = 180 * 1000 * 60L)
    val usage2 = Usage(id = 1, date = calendar.timeInMillis, usage = 123 * 1000 * 60L)
    val usages = listOf(usage, usage2)
    whenever(usageRepository.usages()).thenReturn(Observable.just(usages))

    val testObserver = streakRepository.currentStreak().test()

    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)
    assertEquals(2, testObserver.values()[0])
  }

  @Test fun testCurrentStreak_zeroStreakWithBreak() {
    val goals = GoalSampleData.getSampleGoals(4)
    whenever(goalRepository.goals()).thenReturn(Observable.just(goals))

    val calendar = Calendar.getInstance()

    val usage = Usage(id = 0, date = calendar.timeInMillis, usage = 180 * 1000 * 60L)
    val usage2 = Usage(id = 1, date = calendar.timeInMillis, usage = 123 * 1000 * 60L)
    val usage3 = Usage(id = 2, date = calendar.timeInMillis, usage = 220 * 1000 * 60L)
    val usages = listOf(usage, usage2, usage3)
    whenever(usageRepository.usages()).thenReturn(Observable.just(usages))

    val testObserver = streakRepository.currentStreak().test()

    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)
    assertEquals(0, testObserver.values()[0])
  }

  @Test fun testCurrentStreak_oneStreakWithBreak() {
    val goals = GoalSampleData.getSampleGoals(4)
    whenever(goalRepository.goals()).thenReturn(Observable.just(goals))

    val calendar = Calendar.getInstance()

    val usage = Usage(id = 0, date = calendar.timeInMillis, usage = 280 * 1000 * 60L)
    val usage2 = Usage(id = 1, date = calendar.timeInMillis, usage = 123 * 1000 * 60L)
    val usage3 = Usage(id = 2, date = calendar.timeInMillis, usage = 120 * 1000 * 60L)
    val usages = listOf(usage, usage2, usage3)
    whenever(usageRepository.usages()).thenReturn(Observable.just(usages))

    val testObserver = streakRepository.currentStreak().test()

    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)
    assertEquals(2, testObserver.values()[0])
  }

  @Test fun testCurrentStreak_twoStreakWithBreak() {
    val goals = GoalSampleData.getSampleGoals(4)
    whenever(goalRepository.goals()).thenReturn(Observable.just(goals))

    val calendar = Calendar.getInstance()

    val usage = Usage(id = 0, date = calendar.timeInMillis, usage = 180 * 1000 * 60L)
    val usage2 = Usage(id = 1, date = calendar.timeInMillis, usage = 223 * 1000 * 60L)
    val usage3 = Usage(id = 2, date = calendar.timeInMillis, usage = 120 * 1000 * 60L)
    val usage4 = Usage(id = 3, date = calendar.timeInMillis, usage = 120 * 1000 * 60L)
    val usages = listOf(usage, usage2, usage3, usage4)
    whenever(usageRepository.usages()).thenReturn(Observable.just(usages))

    val testObserver = streakRepository.currentStreak().test()

    testObserver.assertNoErrors()
    testObserver.assertValueCount(1)
    assertEquals(2, testObserver.values()[0])
  }

}