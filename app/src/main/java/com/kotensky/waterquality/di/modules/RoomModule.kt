package com.kotensky.waterquality.di.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.kotensky.waterquality.model.room.AppDatabase
import com.kotensky.waterquality.model.room.dao.StatisticDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "Statistics-app-db").build()
    }

    @Provides
    @Singleton
    fun provideStatisticDao(appDatabase: AppDatabase): StatisticDao {
        return appDatabase.statisticDao()
    }

}