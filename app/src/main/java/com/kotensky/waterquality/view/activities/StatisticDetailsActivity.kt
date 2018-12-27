package com.kotensky.waterquality.view.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kotensky.waterquality.R
import com.kotensky.waterquality.di.components.DaggerScreenComponent
import com.kotensky.waterquality.interfaces.StatisticDetailsItemClickListener
import com.kotensky.waterquality.model.entities.StatisticMainEntity
import com.kotensky.waterquality.view.adapters.StatisticDetailsAdapter
import kotlinx.android.synthetic.main.activity_statistic_details.*
import javax.inject.Inject



class StatisticDetailsActivity : BaseActivity(), OnMapReadyCallback, StatisticDetailsItemClickListener {

    companion object {
        const val STATISTIC_DETAILS_ENTITY_KEY = "statistic_details_entity_key"
    }

    @Inject
    lateinit var adapter: StatisticDetailsAdapter

    private var mainEntity: StatisticMainEntity? = null
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentValues()
        if (mainEntity == null) {
            finish()
        }

        setContentView(R.layout.activity_statistic_details)

        setSupportActionBar(toolbar)
        supportActionBar?.title = mainEntity?.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter.listener = this
        adapter.data = mainEntity?.data
        adapter.notifyDataSetChanged()

        statisticDataRecycler.adapter = adapter
        statisticDataRecycler.layoutManager = LinearLayoutManager(this)
        statisticDataRecycler.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun getIntentValues() {
        try {
            mainEntity = intent.getSerializableExtra(STATISTIC_DETAILS_ENTITY_KEY) as StatisticMainEntity?
        } catch (e: Exception) {
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        mainEntity?.data?.forEachIndexed { index, statisticDataEntity ->

            val position = LatLng(statisticDataEntity?.lat ?: 0.0, statisticDataEntity?.lon ?: 0.0)
            this.googleMap?.addMarker(MarkerOptions().position(position).title("${statisticDataEntity?.time} ${statisticDataEntity?.date}"))

            if (index == 0) {
                this.googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 14F))
            }
        }
    }

    override fun onItemClick(position: Int) {
        val statisticDataEntity = mainEntity?.data?.getOrNull(position) ?: return
        googleMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        LatLng(statisticDataEntity.lat ?: 0.0, statisticDataEntity.lon
                                ?: 0.0), 14F))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.chart_action -> {
                val intent = Intent(this, StatisticChartsActivity::class.java)
                intent.putExtra(STATISTIC_DETAILS_ENTITY_KEY, mainEntity)
                startActivity(intent)
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