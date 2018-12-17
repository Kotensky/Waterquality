package com.kotensky.waterquality.model.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.kotensky.waterquality.model.entities.StatisticMainEntity
import com.kotensky.waterquality.model.room.dao.StatisticDao


@Database(entities = [(StatisticMainEntity::class)], version = 1, exportSchema = false)
@TypeConverters(StatisticTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun statisticDao(): StatisticDao

}