package shaishav.com.bebetter.fragments

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyRecyclerView
import shaishav.com.bebetter.R
import shaishav.com.bebetter.adapter.RecyclerUsageController
import shaishav.com.bebetter.contracts.SummaryContract
import shaishav.com.bebetter.di.DependencyGraph
import shaishav.com.bebetter.di.modules.SummaryModule
import shaishav.com.bebetter.presenter.SummaryPresenter
import javax.inject.Inject

/**
 * Created by shaishav.gandhi on 12/24/17.
 */
class SummaryFragment2: Fragment(), SummaryContract {

    lateinit var rootView: View
    lateinit var recyclerView: EpoxyRecyclerView
    lateinit var adapter: RecyclerUsageController

    @Inject lateinit var presenter: SummaryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity.application is DependencyGraph) {
            (activity.application as DependencyGraph).addSummaryComponent(SummaryModule(this)).inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(R.layout.fragment_day_summary, container, false)
        presenter.view = this
        initViews()

        adapter = RecyclerUsageController(activity.applicationContext)
        recyclerView.setController(adapter)
        return rootView
    }

    fun initViews() {
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity.applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (activity.application is DependencyGraph) {
            (activity.application as DependencyGraph).removeSummaryComponent()
        }
        presenter.disposables.dispose()
    }

    override fun setAverageDaiyUsage(usage: Long) {
        adapter.averageDailyUsage = usage
    }

    override fun setCurrentSession(usage: Long) {
        adapter.currentUsage = usage
    }

    override fun setDailyUsage(usage: Long) {
        adapter.dailyUsage = usage
    }
}