package com.kotensky.waterquality.view.activities

import android.os.Bundle
import android.view.MenuItem
import com.kotensky.waterquality.R
import com.kotensky.waterquality.di.components.DaggerScreenComponent
import com.kotensky.waterquality.model.entities.StatisticMainEntity
import com.kotensky.waterquality.view.adapters.ChartPagerAdapter
import kotlinx.android.synthetic.main.activity_statistic_charts.*


class StatisticChartsActivity : BaseActivity() {

    private var mainEntity: StatisticMainEntity? = null
    private lateinit var adapter: ChartPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentValues()
        if (mainEntity == null) {
            finish()
        }
        setContentView(R.layout.activity_statistic_charts)

        setSupportActionBar(toolbar)
        supportActionBar?.title = mainEntity?.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = ChartPagerAdapter(this, mainEntity, supportFragmentManager)
        chartsPager.adapter = adapter
        chartsPager.offscreenPageLimit = 2
        chartsTabLayout.setupWithViewPager(chartsPager)

    }


    private fun getIntentValues() {
        try {
            mainEntity = intent.getSerializableExtra(StatisticDetailsActivity.STATISTIC_DETAILS_ENTITY_KEY) as StatisticMainEntity?
        } catch (e: Exception) {
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun inject() {
        DaggerScreenComponent.builder()
                .applicationComponent(getApplicationComponent())
                .build()
                .inject(this)
    }
}