package com.kotensky.waterquality.model.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.kotensky.waterquality.model.entities.StatisticDataEntity
import com.kotensky.waterquality.model.room.dao.StatisticDao


@Database(entities = [(StatisticDataEntity::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun statisticDao(): StatisticDao

}