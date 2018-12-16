package com.kotensky.waterquality.di.modules

import android.content.Context
import com.kotensky.waterquality.application.WaterQualityApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(val context: WaterQualityApplication) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = context

}