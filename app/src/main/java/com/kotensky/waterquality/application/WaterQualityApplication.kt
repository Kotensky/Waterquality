package com.kotensky.waterquality.application

import android.app.Application
import com.kotensky.waterquality.di.components.ApplicationComponent
import com.kotensky.waterquality.di.components.DaggerApplicationComponent
import com.kotensky.waterquality.di.modules.ApplicationModule

class WaterQualityApplication : Application() {

    var applicationComponent: ApplicationComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }

    private fun initAppComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }
}