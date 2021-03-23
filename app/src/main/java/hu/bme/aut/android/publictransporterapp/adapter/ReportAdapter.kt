package hu.bme.aut.android.publictransporterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.publictransporterapp.R
import hu.bme.aut.android.publictransporterapp.data.Report
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReportAdapter(val context: Context, val reportTime: Float):
    RecyclerView.Adapter<ReportAdapter.ReportItemViewHolder>() {

    /*private val stationsAll: ArrayList<Station> = ArrayList()
    private val stationTypes: ArrayList<String> = ArrayList()
    private var closestDistance: Int = 1000000000*/

    private val items = mutableListOf<Pair<Report, String>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportItemViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_report_list, parent, false)
        return ReportItemViewHolder(itemView)
    }

    inner class ReportItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val reportType: TextView
        val stationName: TextView
        val stationType: TextView
        val btnUpdateTime: ImageButton

        var item: Pair<Report, String>? = null
        /**
         * Conversion:
         * <variable name> = itemView.findViewById(R.id.<ID of the item, could be find in item_report_list.xml>)
         */

        init {
            reportType = itemView.findViewById(R.id.reporttype)
            stationName = itemView.findViewById(R.id.stationname)
            stationType = itemView.findViewById(R.id.stationType)
            btnUpdateTime = itemView.findViewById(R.id.btnAddMinutes)
            btnUpdateTime.setOnClickListener{
                item?.let { updateTime(it) }
            }
        }
    }

    private fun updateTime(item: Pair<Report, String>) {
        val key = item.second
        val newPost = Report(
            item.first.uid,
            item.first.userName,
            item.first.reportType,
            item.first.latitude,
            item.first.longitude,
            item.first.stationName,
            item.first.transportType,
            item.first.reportDate,
            getTodayPlusTime().toString()
        )

        /*val newPostValues = newPost.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/$reportDateUntil" to newPostValues
        )*/

        FirebaseDatabase.getInstance().reference
            .child("reports")
            .child(key)//.setValue(newPost)
            .setValue(newPost)
            .addOnCompleteListener {
                Toast.makeText(context, R.string.update_message, Toast.LENGTH_SHORT).show()
            }

        /*val key = database.child("posts").push().key ?: return

        val post = Post(userId, username, title, body)
        val postValues = post.toMap()

        val childUpdates = hashMapOf<String, Any>(
            "/posts/$key" to postValues,
            "/user-posts/$userId/$key" to postValues
        )

        database.updateChildren(childUpdates)*/

    }

    private fun getTodayPlusTime(): LocalDateTime {
        return LocalDateTime.now().plusMinutes(reportTime.toLong())
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ReportItemViewHolder, position: Int) {
        val item = items[position]

        /**
         * Conversion
         * holder.<name in class ReportItemViewHolder.text = item.<name in ReportItem>[.toString()]>
         */

        holder.reportType.text = item.first.reportType
        holder.stationType.text = item.first.transportType
        if(item.first.stationName != "NOSTATION"){
            holder.stationName.text = item.first.stationName
        } else {
            holder.stationName.text = "ÚTKÖZBEN"
        }

        holder.item = item
    }

    fun addReport(reportItem: Pair<Report, String>) {
        reportItem.first ?: return
        if(reportItem.first.reportDateUntil!! > getTodayAsString().toString()) {
            items.add(reportItem)
            notifyDataSetChanged()
        }
    }

    private fun getTodayAsString(): LocalDateTime {
        return LocalDateTime.now()
    }

    fun removeReport(reportItem: Pair<Report, String>){
        reportItem ?: return
        items.remove(reportItem)
        notifyDataSetChanged()
    }
}