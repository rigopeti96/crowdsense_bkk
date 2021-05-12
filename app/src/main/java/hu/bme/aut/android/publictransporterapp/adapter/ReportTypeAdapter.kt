package hu.bme.aut.android.publictransporterapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.publictransporterapp.MainActivity
import hu.bme.aut.android.publictransporterapp.R
import hu.bme.aut.android.publictransporterapp.data.Report
import hu.bme.aut.android.publictransporterapp.data.Station
import java.time.LocalDateTime
import java.util.*

/**
 * Implement: add this item to Room database
 */

class ReportTypeAdapter (
    private var context: Context,
    private var errorTypeName: ArrayList<String>,
    private var station: Station,
    private val reportTime: Float

) :
    RecyclerView.Adapter<ReportTypeAdapter.MyViewHolder>() {
    //private lateinit var database: ReportListDatabase
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    val uid: String?
        get() = firebaseUser?.uid

    val userName: String?
        get() = firebaseUser?.displayName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.reporttype_row, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        holder.errorTypeName.text = errorTypeName[position]
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener {
            Toast.makeText(context, R.string.press_long, Toast.LENGTH_SHORT).show()
        }

        if(errorTypeName[position] != "Betöltés"){
            holder.itemView.setOnLongClickListener {
                /*onReportItemCreated(getReportItem(errorTypeName[position]))*/
                uploadPost(errorTypeName[position])
                val restarterIntent = Intent(context, MainActivity::class.java)
                restarterIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                ContextCompat.startActivity(context, restarterIntent, null)
                return@setOnLongClickListener true
            }
        }
    }

    private fun getTodayAsString(): LocalDateTime {
        return LocalDateTime.now()
    }

    private fun getTodayPlusTime(): LocalDateTime {
        return LocalDateTime.now().plusMinutes(reportTime.toLong())
    }

    override fun getItemCount(): Int {
        return errorTypeName.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var errorTypeName: TextView = itemView.findViewById<View>(R.id.tvErrorType) as TextView
    }


    private fun uploadPost(reportTypeName: String) {
        val key = FirebaseDatabase.getInstance().reference.child("reports").push().key ?: return
        val newPost = Report(
            uid,
            userName,
            reportTypeName,
            station.latitude,
            station.longitude,
            station.name,
            station.stopType,
            getTodayAsString().toString(),
            getTodayPlusTime().toString()
            )

        FirebaseDatabase.getInstance().reference
            .child("reports")
            .child(key)
            .setValue(newPost)
            .addOnCompleteListener {
                Toast.makeText(context, R.string.thanks_message, Toast.LENGTH_SHORT).show()
            }
    }
}