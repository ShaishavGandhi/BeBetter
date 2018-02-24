package shaishav.com.bebetter.presenter

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.junit.Before
import org.junit.Ignore
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import shaishav.com.bebetter.contracts.SummaryContract
import shaishav.com.bebetter.data.repository.GoalRepository
import shaishav.com.bebetter.data.repository.UsageRepository

/**
 * Created by shaishav.gandhi on 2/15/18.
 */
@RunWith(JUnit4::class)
@Ignore("Fix later")
class SummaryPresenterUTest {

  companion object {
    val currentSession = 1000 * 10 * 20L
    val dailyUsage = 1000 * 60 * 60L
    val averageDailyUsage = 1000 * 60 * 35L
  }

  @Mock lateinit var view: SummaryContract
  @Mock lateinit var usageRepository: UsageRepository
  @Mock lateinit var goalRepository: GoalRepository
  lateinit var presenter: SummaryPresenter

  @Before
  @Throws
  fun setUp() {
    setupData()
    presenter = SummaryPresenter(view, usageRepository, goalRepository, CompositeDisposable())
  }

  fun setupData() {
    whenever(usageRepository.currentSession()).thenReturn(Observable.just(currentSession))
    whenever(usageRepository.dailyUsage()).thenReturn(Observable.just(dailyUsage))
    whenever(usageRepository.averageDailyUsage()).thenReturn(Observable.just(averageDailyUsage))
  }

}