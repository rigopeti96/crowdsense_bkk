package hu.bme.aut.android.publictransporterapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * add public transport type to this data class!!!
 */

@Entity(tableName = "reportItem")
data class ReportItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "reporttype") val reportType: ReportType,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "stationname") val stationName: String
    //@ColumnInfo(name = "reportdate") val reportDate: LocalDateTime
)