package hu.bme.aut.android.publictransporterapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.publictransporterapp.R
import hu.bme.aut.android.publictransporterapp.TrafficErrorActivity
import java.util.*

class StationAdapter(
    private var context: Context,
    private var stationName: ArrayList<String>,
    private var latitude: ArrayList<String>,
    private var longitude: ArrayList<String>
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
    }
}