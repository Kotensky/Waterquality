package com.kotensky.waterquality.utils

import com.kotensky.waterquality.model.entities.StatisticDataEntity
import java.text.SimpleDateFormat
import java.util.*



fun sortDataListByDate(dataMutableList: MutableList<StatisticDataEntity?>?, locale: Locale){
    val fullDateFormat = SimpleDateFormat("HH:mm:ss,dd.MM.yyyy", locale)

    dataMutableList?.sortWith(Comparator { o1, o2 ->
        fullDateFormat.parse("${o1?.time},${o1?.date}").time.compareTo(
                fullDateFormat.parse("${o2?.time},${o2?.date}").time ) })
}