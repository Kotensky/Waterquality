package com.kotensky.waterquality.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.kotensky.waterquality.R
import com.kotensky.waterquality.di.components.DaggerScreenComponent
import com.kotensky.waterquality.interfaces.ListItemClickListener
import com.kotensky.waterquality.interfaces.view.DBView
import com.kotensky.waterquality.model.entities.StatisticMainEntity
import com.kotensky.waterquality.presenter.DBPresenter
import com.kotensky.waterquality.view.activities.ImportStatisticActivity.Companion.ADD_STATISTIC_REQUEST_CODE
import com.kotensky.waterquality.view.activities.StatisticDetailsActivity.Companion.STATISTIC_DETAILS_ENTITY_KEY
import com.kotensky.waterquality.view.adapters.StatisticMainAdapter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), DBView, ListItemClickListener {


    @Inject
    lateinit var presenter: DBPresenter
    @Inject
    lateinit var statisticMainAdapter: StatisticMainAdapter

    private val statistics = ArrayList<StatisticMainEntity?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        presenter.view = this

        statisticMainAdapter.listener = this
        statisticMainAdapter.statistics = statistics

        dataRecycler.adapter = statisticMainAdapter
        dataRecycler.layoutManager = LinearLayoutManager(this)

        addWaterDataFab.setOnClickListener { view ->
            startActivityForResult(Intent(this, ImportStatisticActivity::class.java), ADD_STATISTIC_REQUEST_CODE)
        }

        dataSwipeRefresh.setOnRefreshListener {
            presenter.loadData()
        }
        presenter.loadData()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == ADD_STATISTIC_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            presenter.loadData()
        }
    }

    override fun showData(statistics: List<StatisticMainEntity?>) {
        this.statistics.clear()
        this.statistics.addAll(statistics)
        statisticMainAdapter.notifyDataSetChanged()
        if (statistics.isEmpty()){
            dataBaseEmptyTxt.visibility = View.VISIBLE
            dataSwipeRefresh.visibility = View.GONE
        } else {
            dataBaseEmptyTxt.visibility = View.GONE
            dataSwipeRefresh.visibility = View.VISIBLE
        }
    }

    override fun hideLoading() {
        dataSwipeRefresh.isRefreshing = false
        if (statistics.isEmpty()){
            dataBaseEmptyTxt.visibility = View.VISIBLE
            dataSwipeRefresh.visibility = View.GONE
        } else {
            dataBaseEmptyTxt.visibility = View.GONE
            dataSwipeRefresh.visibility = View.VISIBLE
        }
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, StatisticDetailsActivity::class.java)
        intent.putExtra(STATISTIC_DETAILS_ENTITY_KEY, statistics.getOrNull(position))
        startActivity(intent)
    }

    override fun inject() {
        DaggerScreenComponent.builder()
                .applicationComponent(getApplicationComponent())
                .build()
                .inject(this)
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }
}
