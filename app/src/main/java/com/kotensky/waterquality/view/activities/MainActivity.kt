package com.kotensky.waterquality.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.kotensky.waterquality.R
import com.kotensky.waterquality.di.components.DaggerScreenComponent
import com.kotensky.waterquality.view.activities.AddStatisticActivity.Companion.ADD_STATISTIC_REQUEST_CODE
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        addWaterDataFab.setOnClickListener { view ->
            startActivityForResult(Intent(this, AddStatisticActivity::class.java), ADD_STATISTIC_REQUEST_CODE)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == ADD_STATISTIC_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            presenter.loadData()
        }
    }

    override fun inject() {
        DaggerScreenComponent.builder()
                .applicationComponent(getApplicationComponent())
                .build()
                .inject(this)
    }
}
