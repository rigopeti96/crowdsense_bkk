package hu.bme.aut.android.publictransporterapp.data

import java.time.LocalDateTime

class Report(
    var uid: String? = null,
    var userName: String? = null,
    var reportType: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var stationName: String? = null,
    var transportType: String? = null,
    var reportDate: String? = null,
    val reportDateUntil: String? = null
)