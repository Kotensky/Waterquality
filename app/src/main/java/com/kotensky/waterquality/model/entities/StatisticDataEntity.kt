package com.kotensky.waterquality.model.entities

import java.io.Serializable


class StatisticDataEntity(
        var time: String? = null,
        var date: String? = null,
        var lat: Double? = null,
        var lon: Double? = null,
        var temperature: Float? = null,
        var ph: Float? = null,
        var ppm: Float? = null) : Serializable {

    override fun toString(): String {
        return "$time;$date;$lat;$lon;$temperature;$ph;$ppm;"
    }
}