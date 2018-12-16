package com.kotensky.waterquality.model.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "statistic")
class StatisticDataEntity(
        @PrimaryKey(autoGenerate = true)
        var id: Long? = null,
        var name: String,
        var lat: Long,
        var lon: Long,
        var temperature: Float,
        var ph: Float,
        var ppm: Float)