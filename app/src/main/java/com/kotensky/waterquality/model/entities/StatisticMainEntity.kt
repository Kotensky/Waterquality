package com.kotensky.waterquality.model.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "statistic")
data class StatisticMainEntity(@PrimaryKey(autoGenerate = true)
                               var id: Long? = null,
                               var name: String? = null,
                               var data: List<StatisticDataEntity?>? = null) : Serializable