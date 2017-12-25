package shaishav.com.bebetter.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import shaishav.com.bebetter.contracts.SummaryContract
import shaishav.com.bebetter.data.repository.UsageRepository
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/17/17.
 */
class SummaryPresenter @Inject constructor(
        var view : SummaryContract,
        val repository: UsageRepository,
        val disposables: CompositeDisposable) {

    init {
        currentSession()
        dailyUsage()
        averageDailyUsage()
    }


    fun currentSession() {
        val disposable = repository
                .currentSession()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view.setCurrentSession(it)
                }
        disposables.add(disposable)
    }

    fun dailyUsage() {
        val disposable = repository
                .dailyUsage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view.setDailyUsage(it)
                }

        disposables.add(disposable)
    }

    fun averageDailyUsage() {
        val disposable = repository
                .averageDailyUsage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view.setAverageDaiyUsage(it)
                }

        disposables.add(disposable)
    }
}