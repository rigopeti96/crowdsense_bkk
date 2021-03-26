package hu.bme.aut.android.publictransporterapp.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.publictransporterapp.R
import hu.bme.aut.android.publictransporterapp.ReportTypeChooserActivity
import hu.bme.aut.android.publictransporterapp.data.Station
import java.util.*


class StationAdapter(
    private var context: Context,
    private var stations: ArrayList<Station> = ArrayList()
) :
    RecyclerView.Adapter<StationAdapter.MyViewHolder>() {

    private var metroSign: String = "[M]"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.station_rows, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        holder.stationName.text = stations[position].name
        /*holder.latitude.text = stations[position].latitude.toString()
        holder.longitude.text = stations[position].longitude.toString()*/
        holder.stoptype.text = stations[position].stopType

        when(stations[position].stopType){
            "BUS" -> holder.itemView.setBackgroundColor(Color.argb(100, 5, 149, 214))
            "TRAM" ->  holder.itemView.setBackgroundColor(Color.YELLOW)
            "TROLLEYBUS" -> holder.itemView.setBackgroundColor(Color.RED)
            "TROLLEYBUS-BUS" -> holder.itemView.setBackgroundColor(Color.RED)
            "NIGHTBUS" -> setNightbusParams(holder)
            "M1" -> setMetroParams(holder, stations[position].name, "M1")
            "M2" -> setMetroParams(holder, stations[position].name, "M2")
            "M3" -> setMetroParams(holder, stations[position].name, "M3")
            "M4" -> setMetroParams(holder, stations[position].name, "M4")
            else -> {
                holder.itemView.setBackgroundColor(Color.argb(100, 156, 39, 176))
            }
        }

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            Toast.makeText(context, stations[position].name, Toast.LENGTH_SHORT).show()
            val trafficIntent = Intent(context, ReportTypeChooserActivity::class.java)
            trafficIntent.putExtra("actualLat", stations[position].latitude)
            trafficIntent.putExtra("actualLong", stations[position].longitude)
            trafficIntent.putExtra("stationName", stations[position].name)
            trafficIntent.putExtra("stoptype", stations[position].stopType)
            startActivity(context, trafficIntent, null)
        }
    }

    fun setNightbusParams(holder: MyViewHolder){
        holder.itemView.setBackgroundColor(Color.BLACK)
        holder.stationName.setTextColor(Color.WHITE)
        /*holder.latitude.setTextColor(Color.WHITE)
        holder.longitude.setTextColor(Color.WHITE)*/
        holder.stoptype.setTextColor(Color.WHITE)
    }

    fun setMetroParams(holder: MyViewHolder, stopType: String, metrotype: String){
        when(metrotype){
            "M1" -> holder.itemView.setBackgroundColor(Color.YELLOW)
            "M2" -> holder.itemView.setBackgroundColor(Color.RED)
            "M3" -> holder.itemView.setBackgroundColor(Color.BLUE)
            "M4" -> holder.itemView.setBackgroundColor(Color.GREEN)
        }
        holder.stationName.text = "$stopType $metroSign"
    }

    override fun getItemCount(): Int {
        return stations.size
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var stationName: TextView = itemView.findViewById<View>(R.id.tvStationName) as TextView
        /*var latitude: TextView = itemView.findViewById<View>(R.id.tvLatitude) as TextView
        var longitude: TextView = itemView.findViewById<View>(R.id.tvLongitude) as TextView*/
        var stoptype: TextView = itemView.findViewById<View>(R.id.tvType) as TextView
    }
}