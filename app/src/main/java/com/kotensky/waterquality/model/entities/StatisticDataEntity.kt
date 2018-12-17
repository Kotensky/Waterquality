package com.kotensky.waterquality.model.entities


class StatisticDataEntity(
        var name: String,
        var lat: Long,
        var lon: Long,
        var temperature: Float,
        var ph: Float,
        var ppm: Float)