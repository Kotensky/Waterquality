package com.kotensky.waterquality.view.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.kotensky.waterquality.application.WaterQualityApplication
import com.kotensky.waterquality.di.components.ApplicationComponent

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    fun getApplicationComponent(): ApplicationComponent? =
            (application as WaterQualityApplication).applicationComponent

    fun showToast(text: String?) {
        if (text.isNullOrEmpty())
            return
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    protected abstract fun inject()

}