package hu.bme.aut.android.publictransporterapp.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.publictransporterapp.R
import hu.bme.aut.android.publictransporterapp.TrafficErrorActivity
import java.util.ArrayList

/**
 * Implement: add this item to Room database
 */

class ReportTypeAdapter (
    private var context: Context,
    private var errorTypeName: ArrayList<String>
) :
    RecyclerView.Adapter<ReportTypeAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.reporttype_row, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        holder.errorTypeName.text = errorTypeName[position]
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            Toast.makeText(context, errorTypeName[position], Toast.LENGTH_SHORT).show()
            val trafficIntent = Intent(context, TrafficErrorActivity::class.java)
            ContextCompat.startActivity(context, trafficIntent, null)
        }
    }

    override fun getItemCount(): Int {
        return errorTypeName.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var errorTypeName: TextView = itemView.findViewById<View>(R.id.tvErrorType) as TextView
    }
}