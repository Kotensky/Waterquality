package com.kotensky.waterquality.model.entities

import java.io.Serializable


class StatisticDataEntity(
        var date: String?,
        var time: String?,
        var lat: Double?,
        var lon: Double?,
        var temperature: Float?,
        var ph: Float?,
        var ppm: Float?) : Serializable