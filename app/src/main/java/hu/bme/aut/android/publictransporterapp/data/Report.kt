package hu.bme.aut.android.publictransporterapp.data

import com.google.firebase.database.Exclude

data class Report(
    var uid: String? = null,
    var userName: String? = null,
    var reportType: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var stationName: String? = null,
    var transportType: String? = null,
    var reportDate: String? = null,
    val reportDateUntil: String? = null
){
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "userName" to userName,
            "reportType" to reportType,
            "latitude" to latitude,
            "longitude" to longitude,
            "stationName" to stationName,
            "transportType" to transportType,
            "reportDate" to reportDate,
            "reportDateUntil" to reportDateUntil
        )
    }
}