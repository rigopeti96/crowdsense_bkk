package hu.bme.aut.android.publictransporterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import hu.bme.aut.android.publictransporterapp.R
import java.util.*

class StationAdapter (
    private var context: Context,
    private var personNames: ArrayList<String>,
    private var emailIds: ArrayList<String>,
    private var mobileNumbers: ArrayList<String>
) :
    RecyclerView.Adapter<StationAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.station_rows, parent, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        holder.name.text = personNames[position]
        holder.email.text = emailIds[position]
        holder.mobileNo.text = mobileNumbers[position]
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            Toast.makeText(context, personNames[position], Toast.LENGTH_SHORT).show()
        }
    }
    override fun getItemCount(): Int {
        return personNames.size
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById<View>(R.id.tvName) as TextView
        var email: TextView = itemView.findViewById<View>(R.id.tvEmail) as TextView
        var mobileNo: TextView = itemView.findViewById<View>(R.id.tvMobile) as TextView
    }
}