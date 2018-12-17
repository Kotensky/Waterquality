package com.kotensky.waterquality.model.room

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kotensky.waterquality.model.entities.StatisticDataEntity
import java.util.*



class StatisticTypeConverters {

    var gson = Gson()

    @TypeConverter
    fun stringToStatisticDataList(data: String?): List<StatisticDataEntity> {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<StatisticDataEntity>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun statisticDataListToString(someObjects: List<StatisticDataEntity>): String {
        return gson.toJson(someObjects)
    }
}