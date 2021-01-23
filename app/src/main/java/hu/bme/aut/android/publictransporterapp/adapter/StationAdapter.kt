package hu.bme.aut.android.publictransporterapp.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.publictransporterapp.R
import hu.bme.aut.android.publictransporterapp.TrafficErrorActivity
import java.util.*

class StationAdapter(
    private var context: Context,
    private var stationName: ArrayList<String>,
    private var latitude: ArrayList<String>,
    private var longitude: ArrayList<String>,
    private var stoptype: ArrayList<String>
) :
    RecyclerView.Adapter<StationAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.station_rows, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        holder.stationName.text = stationName[position]
        holder.latitude.text = latitude[position]
        holder.longitude.text = longitude[position]
        holder.stoptype.text = stoptype[position]
        if(stoptype[position] == "BUS"){
            holder.itemView.setBackgroundColor(Color.argb(100, 5, 149, 214))
        } else if(stoptype[position] == "TRAM") {
            holder.itemView.setBackgroundColor(Color.YELLOW)
        } else if(stoptype[position] == "TROLLEYBUS" || stoptype[position] == "TROLLEYBUS-BUS") {
            holder.itemView.setBackgroundColor(Color.RED)
        } else if(stoptype[position] == "NIGHTBUS") {
            holder.itemView.setBackgroundColor(Color.BLACK)
            holder.stationName.setTextColor(Color.WHITE)
            holder.latitude.setTextColor(Color.WHITE)
            holder.longitude.setTextColor(Color.WHITE)
            holder.stoptype.setTextColor(Color.WHITE)
        } else if(stoptype[position] == "M1") {
            holder.itemView.setBackgroundColor(Color.YELLOW)
            holder.stationName.text = stationName[position] + " " + R.string.metrostation
        } else if(stoptype[position] == "M2") {
            holder.itemView.setBackgroundColor(Color.RED)
            holder.stationName.text = stationName[position] + " " + R.string.metrostation
        } else if(stoptype[position] == "M3") {
            holder.itemView.setBackgroundColor(Color.BLUE)
            holder.stationName.text = stationName[position] + " " + R.string.metrostation
        } else if(stoptype[position] == "M4") {
            holder.itemView.setBackgroundColor(Color.GREEN)
            holder.stationName.text = stationName[position] + " " + R.string.metrostation
        }
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            Toast.makeText(context, stationName[position], Toast.LENGTH_SHORT).show()
            val trafficIntent = Intent(context, TrafficErrorActivity::class.java)
            startActivity(context, trafficIntent, null)
        }
        /*holder.itemView.next_button.setOnClickListener {
            val trafficIntent = Intent(context, PlaceholderActivity::class.java)
            ContextCompat.startActivity(context, trafficIntent, null)
        }*/
    }

    override fun getItemCount(): Int {
        return stationName.size
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var stationName: TextView = itemView.findViewById<View>(R.id.tvStationName) as TextView
        var latitude: TextView = itemView.findViewById<View>(R.id.tvLatitude) as TextView
        var longitude: TextView = itemView.findViewById<View>(R.id.tvLongitude) as TextView
        var stoptype: TextView = itemView.findViewById<View>(R.id.tvType) as TextView
    }
}