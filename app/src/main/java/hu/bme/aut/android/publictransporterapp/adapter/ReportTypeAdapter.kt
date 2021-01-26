package hu.bme.aut.android.publictransporterapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import hu.bme.aut.android.publictransporterapp.R
import hu.bme.aut.android.publictransporterapp.data.ReportItem
import hu.bme.aut.android.publictransporterapp.data.ReportListDatabase
import java.util.ArrayList
import kotlin.concurrent.thread

/**
 * Implement: add this item to Room database
 */

class ReportTypeAdapter (
    private var context: Context,
    private var errorTypeName: ArrayList<String>,
    private var latitude: Double,
    private var longitude: Double,
    private var stationName: String,
    private var stopType: String

) :
    RecyclerView.Adapter<ReportTypeAdapter.MyViewHolder>() {
    private lateinit var database: ReportListDatabase


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        database = Room.databaseBuilder(
            context,
            ReportListDatabase::class.java,
            "report-list2"
        ).build()
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.reporttype_row, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        holder.errorTypeName.text = errorTypeName[position]
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            /*Toast.makeText(context, errorTypeName[position], Toast.LENGTH_SHORT).show()
            val trafficIntent = Intent(context, TrafficErrorActivity::class.java)
            ContextCompat.startActivity(context, trafficIntent, null)*/

            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.exit_dialog_title)
            builder.setMessage(R.string.confirm_dialog_message)

            builder.setPositiveButton(R.string.positive_button_text) { dialog, which ->
                    onReportItemCreated(getReportItem(errorTypeName[position]))
            }

            builder.setNegativeButton(R.string.negative_button_text) { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        }
    }

    override fun getItemCount(): Int {
        return errorTypeName.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var errorTypeName: TextView = itemView.findViewById<View>(R.id.tvErrorType) as TextView
    }

    private fun onReportItemCreated(newItem: ReportItem){
        thread {
            database.reportItemDao().insert(newItem)
            /*val newReportItem = newItem.copy(
                id = newId
            )*/
            /*runOnUiThread {
                adapter.addItem(newReportItem)
            }*/
        }
        Log.d("Lefutott? ", "Lefutott!")
    }

    private fun getReportItem(errorType: String) =
        ReportItem(
            id = null,
            reportType = errorType,
            latitude = latitude,
            longitude = longitude,
            stationName = stationName,
            transportType = stopType
        )

}