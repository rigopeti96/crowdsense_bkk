package hu.bme.aut.android.publictransporterapp.data

import androidx.room.*
import hu.bme.aut.android.publictransporterapp.data.ReportItem

@Dao
interface ReportItemDao {
    @Query("SELECT * FROM reportItem")
    fun getAll(): List<ReportItem>

    /**
     * Query to select items depends on their report type
     */

    @Query("SELECT * FROM reportItem WHERE reporttype = :reportType")
    fun getTrafOrCond(reportType: ReportType): List<ReportItem>

    @Insert
    fun insert(reportItems: ReportItem): Long

    @Update
    fun update(reportItems: ReportItem)

    /**
     * when the Room will be changed for Firebase/backend, this has to be removed
     */

    @Delete
    fun deleteItem(reportItems: ReportItem)
}