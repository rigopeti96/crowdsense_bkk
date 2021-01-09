package hu.bme.aut.android.publictransporterapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.publictransporterapp.R
import hu.bme.aut.android.publictransporterapp.data.ReportItem
import hu.bme.aut.android.publictransporterapp.data.ReportType

class ReportAdapter(private val listener: ReportItemClickListener):
    RecyclerView.Adapter<ReportAdapter.ReportItemViewHolder>() {

    private val items = mutableListOf<ReportItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportItemViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_report_list, parent, false)
        return ReportItemViewHolder(itemView)
    }

    interface ReportItemClickListener {
        fun onItemChanged(item: ReportItem)
        fun deleteItem(item: ReportItem)
    }

    inner class ReportItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val reportTypeIcon: ImageView
        val reportType: TextView
        val latitude: TextView
        val longitude: TextView
        val stationName: TextView
        val removeBtn: ImageButton

        var item: ReportItem? = null

        /**
         * Conversion:
         * <variable name> = itemView.findViewById(R.id.<ID of the item, could be find in item_report_list.xml>)
         */

        init {
            reportTypeIcon = itemView.findViewById(R.id.type_icon)
            reportType = itemView.findViewById(R.id.reporttype)

            /**
             * Latitude and longitude is just for debug and test. When firebase (or some other backend) is
             * used: removeable
             */

            latitude = itemView.findViewById(R.id.latitude)
            longitude = itemView.findViewById(R.id.longitude)

            /**
             * These others are necessary
             */

            stationName = itemView.findViewById(R.id.stationname)
            removeBtn = itemView.findViewById(R.id.removebtn)
            removeBtn.setOnClickListener{
                if(item != null){
                    items.remove(item!!)
                    listener.deleteItem(item!!)
                    notifyDataSetChanged()
                }
            }
        }
    }

    @DrawableRes
    private fun getImageResource(type: ReportType) = when (type) {
        ReportType.TRAFFIC -> R.mipmap.alert_icon
        ReportType.CONDUCTOR -> R.mipmap.ticket_icon
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ReportItemViewHolder, position: Int) {
        val item = items[position]

        /**
         * Conversion
         * holder.<name in class ReportItemViewHolder.text = item.<name in ReportItem>[.toString()]>
         */

        holder.reportTypeIcon.setImageResource(getImageResource(item.reportType))
        holder.reportType.text = item.reportType.name
        holder.latitude.text = item.latitude.toString()
        holder.longitude.text = item.longitude.toString()
        holder.stationName.text = item.stationName

        holder.item = item
    }

    fun addItem(item: ReportItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(reportItems: List<ReportItem>) {
        items.clear()
        items.addAll(reportItems)
        notifyDataSetChanged()
    }
}