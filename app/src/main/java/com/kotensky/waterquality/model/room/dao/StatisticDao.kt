package com.kotensky.waterquality.model.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.kotensky.waterquality.model.entities.StatisticMainEntity
import io.reactivex.Single

@Dao
interface StatisticDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStatistic(statisticMainEntity: StatisticMainEntity)

    @Query("SELECT * FROM statistic")
    fun getAllStatistics(): Single<List<StatisticMainEntity?>>

}