package hu.bme.aut.android.publictransporterapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.bme.aut.android.publictransporterapp.data.ReportType
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "reportItem")
data class ReportItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "reporttype") val reportType: ReportType,
    @ColumnInfo(name = "latitude") val latitude: Long,
    @ColumnInfo(name = "longitude") val longitude: Long,
    @ColumnInfo(name = "stationname") val stationName: String
    //@ColumnInfo(name = "reportdate") val reportDate: LocalDateTime
)