package com.kotensky.waterquality.model.room.dao

import android.arch.persistence.room.*
import com.kotensky.waterquality.model.entities.StatisticMainEntity
import io.reactivex.Single

@Dao
interface StatisticDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStatistic(statisticMainEntity: StatisticMainEntity)

    @Delete
    fun deleteStatistic(statisticMainEntity: StatisticMainEntity)

    @Query("SELECT * FROM statistic")
    fun getAllStatistics(): Single<List<StatisticMainEntity?>>

}