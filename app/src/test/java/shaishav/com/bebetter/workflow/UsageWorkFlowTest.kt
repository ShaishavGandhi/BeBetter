package shaishav.com.bebetter.workflow

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import shaishav.com.bebetter.data.models.Goal
import shaishav.com.bebetter.data.models.Point
import shaishav.com.bebetter.data.models.Usage
import shaishav.com.bebetter.data.repository.GoalRepository
import shaishav.com.bebetter.data.repository.PointsRepository
import shaishav.com.bebetter.data.repository.StreakRepository
import shaishav.com.bebetter.data.repository.UsageRepository
import java.util.*

/**
 * Created by shaishav.gandhi on 3/12/18.
 */
@RunWith(JUnit4::class)
class UsageWorkFlowTest {

  @Mock lateinit var usageRepository: UsageRepository
  @Mock lateinit var goalRepository: GoalRepository
  @Mock lateinit var pointsRepository: PointsRepository
  @Mock lateinit var streakRepository: StreakRepository
  lateinit var workflow: UsageWorkflow

  @Before @Throws fun setup() {
    MockitoAnnotations.initMocks(this)
    workflow = UsageWorkflow(usageRepository, goalRepository, pointsRepository, streakRepository)
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

  @Test fun testAddPointsWithAchievedGoal() {
    val currentTime = Date().time
    val usage = Usage(id = 0, date = currentTime, usage = 100 * 1000 * 60)

    val sampleGoal = Goal(id = 0, date = currentTime, goal = 200 * 1000 * 60)
    whenever(goalRepository.goal(currentTime)).thenReturn(Observable.just(sampleGoal))
    whenever(streakRepository.currentStreak()).thenReturn(Observable.just(0))

    var extraPoints = (sampleGoal.goal - usage.usage).toDouble() / (sampleGoal.goal * sampleGoal.goal).toDouble()
    extraPoints *= Math.pow(10.0, 9.0)
    val points = 50 + Math.round(extraPoints).toInt()
    val point = Point(id = 0, date = currentTime, points = points)
    whenever(pointsRepository.save(point)).thenReturn(Completable.complete())

    workflow.addPoints(currentTime, usage)


    verify(pointsRepository).save(point)
  }

  @Test fun testAddPointsWithAchievedGoalWithStreak() {
    val currentTime = Date().time
    val usage = Usage(id = 0, date = currentTime, usage = 100 * 1000 * 60)
    val streak = 2L

    val sampleGoal = Goal(id = 0, date = currentTime, goal = 200 * 1000 * 60)
    whenever(goalRepository.goal(currentTime)).thenReturn(Observable.just(sampleGoal))
    whenever(streakRepository.currentStreak()).thenReturn(Observable.just(streak))

    var extraPoints = (sampleGoal.goal - usage.usage).toDouble() / (sampleGoal.goal * sampleGoal.goal).toDouble()
    extraPoints *= Math.pow(10.0, 9.0)
    extraPoints *= streak

    val points = 50 + Math.round(extraPoints).toInt()
    val point = Point(id = 0, date = currentTime, points = points)
    whenever(pointsRepository.save(point)).thenReturn(Completable.complete())

    workflow.addPoints(currentTime, usage)

    verify(pointsRepository).save(point)
  }

  @Test fun testAddPointsWithAlmostAchievedGoalWithStreak() {
    val currentTime = Date().time
    val usage = Usage(id = 0, date = currentTime, usage = 190 * 1000 * 60)
    val streak = 2L

    val sampleGoal = Goal(id = 0, date = currentTime, goal = 200 * 1000 * 60)
    whenever(goalRepository.goal(currentTime)).thenReturn(Observable.just(sampleGoal))
    whenever(streakRepository.currentStreak()).thenReturn(Observable.just(streak))

    var extraPoints = (sampleGoal.goal - usage.usage).toDouble() / (sampleGoal.goal * sampleGoal.goal).toDouble()
    extraPoints *= Math.pow(10.0, 9.0)
    extraPoints *= streak

    val points = 50 + Math.round(extraPoints).toInt()
    val point = Point(id = 0, date = currentTime, points = points)
    whenever(pointsRepository.save(point)).thenReturn(Completable.complete())

    workflow.addPoints(currentTime, usage)

    verify(pointsRepository).save(point)
  }

  @Test fun testAddPointsWithNotAchievedGoal() {
    val currentTime = Date().time
    val usage = Usage(id = 0, date = currentTime, usage = 201 * 1000 * 60)
    val streak = 2L

    val sampleGoal = Goal(id = 0, date = currentTime, goal = 200 * 1000 * 60)
    whenever(goalRepository.goal(currentTime)).thenReturn(Observable.just(sampleGoal))
    whenever(streakRepository.currentStreak()).thenReturn(Observable.just(streak))
    val point = Point(id = 0, date = currentTime, points = 0)

    whenever(pointsRepository.save(point)).thenReturn(Completable.complete())

    workflow.addPoints(currentTime, usage)

    verify(pointsRepository).save(point)
  }
}