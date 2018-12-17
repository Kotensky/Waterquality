package com.kotensky.waterquality.model.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "statistic")
data class StatisticMainEntity(@PrimaryKey(autoGenerate = true)
                               var id: Long? = null,
                               var name: String? = null,
                               var data: List<StatisticDataEntity?>?)