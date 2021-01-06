package hu.bme.aut.android.publictransporterapp.data

import androidx.room.*
import hu.bme.aut.android.publictransporterapp.data.ReportItem

@Dao
interface ReportItemDao {
    @Query("SELECT * FROM reportItem")
    fun getAll(): List<ReportItem>

    /**
     * Query to select all of the items with report type "Traffic"
     */

    @Query("SELECT * FROM reportItem WHERE reporttype = 'TRAFFIC'")
    fun getTraffic(): List<ReportItem>

    /**
     * Query to select all of the items with report type "Conductor"
     */

    @Query("SELECT * FROM reportItem WHERE reporttype = 'CONDUCTOR'")
    fun getConductor(): List<ReportItem>

    @Insert
    fun insert(cashflowItems: ReportItem): Long

    @Update
    fun update(cashflowItems: ReportItem)

    /**
     * when the Room will be changed for Firebase/backend, this has to be removed
     */

    @Delete
    fun deleteItem(cashflowItems: ReportItem)
}