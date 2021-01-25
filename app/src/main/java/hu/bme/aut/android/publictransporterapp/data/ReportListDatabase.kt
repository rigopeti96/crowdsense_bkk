package hu.bme.aut.android.publictransporterapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ReportItem::class], version = 1)
abstract class ReportListDatabase: RoomDatabase() {
    abstract fun reportItemDao(): ReportItemDao
}