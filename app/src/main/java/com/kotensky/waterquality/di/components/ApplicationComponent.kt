package com.kotensky.waterquality.di.components

import android.content.Context
import com.kotensky.waterquality.di.modules.ApplicationModule
import com.kotensky.waterquality.di.modules.RoomModule
import com.kotensky.waterquality.model.room.dao.StatisticDao
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [(ApplicationModule::class), (RoomModule::class)])
interface ApplicationComponent {

    fun statisticDao(): StatisticDao
    fun context(): Context
}
