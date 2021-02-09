package hu.bme.aut.android.publictransporterapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import hu.bme.aut.android.publictransporterapp.MainActivity
import hu.bme.aut.android.publictransporterapp.R
import hu.bme.aut.android.publictransporterapp.data.ReportItem
import hu.bme.aut.android.publictransporterapp.data.ReportListDatabase
import hu.bme.aut.android.publictransporterapp.data.Station
import java.util.ArrayList
import kotlin.concurrent.thread

/**
 * Implement: add this item to Room database
 */

class ReportTypeAdapter (
    private var context: Context,
    private var errorTypeName: ArrayList<String>,
    private var station: Station

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
        /*holder.itemView.setOnClickListener { // display a toast with person name on item click

            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.exit_dialog_title)
            builder.setMessage(R.string.confirm_dialog_message)

            builder.setPositiveButton(R.string.positive_button_text) { dialog, which ->
                onReportItemCreated(getReportItem(errorTypeName[position]))
                val restarterIntent = Intent(context, MainActivity::class.java)
                Toast.makeText(context, R.string.thanks_message, Toast.LENGTH_SHORT).show()
                restarterIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ContextCompat.startActivity(context, restarterIntent, null)
            }

            builder.setNegativeButton(R.string.negative_button_text) { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        }*/
        holder.itemView.setOnLongClickListener {
            onReportItemCreated(getReportItem(errorTypeName[position]))
            val restarterIntent = Intent(context, MainActivity::class.java)
            Toast.makeText(context, R.string.thanks_message, Toast.LENGTH_SHORT).show()
            restarterIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            ContextCompat.startActivity(context, restarterIntent, null)
            return@setOnLongClickListener true
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
        }
    }

    private fun getReportItem(errorType: String) =
        ReportItem(
            id = null,
            reportType = errorType,
            latitude = station.latitude,
            longitude = station.longitude,
            stationName = station.name,
            transportType = station.stopType
        )

}