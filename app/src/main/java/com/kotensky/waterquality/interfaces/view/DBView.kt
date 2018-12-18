package com.kotensky.waterquality.interfaces.view

import com.kotensky.waterquality.model.entities.StatisticMainEntity

interface DBView {

    fun onStatisticAdded() {}

    fun showData(statistics: List<StatisticMainEntity?>) {}

    fun hideLoading() {}

}