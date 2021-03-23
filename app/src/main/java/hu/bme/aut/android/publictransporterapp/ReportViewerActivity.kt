package hu.bme.aut.android.publictransporterapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.publictransporterapp.adapter.ReportAdapter
import hu.bme.aut.android.publictransporterapp.data.Report
import kotlinx.android.synthetic.main.content_report.*

class ReportViewerActivity: AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReportAdapter
    private var actualTimePlus = 5F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        actualTimePlus = sharedPreferences.getFloat("time", 5F)

        initRecyclerView()
        initPostsListener()
    }

    /**
     * Database functions
     */

    private fun initRecyclerView() {
        recyclerView = ReportRecyclerView
        adapter = ReportAdapter(applicationContext, actualTimePlus)
        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            reverseLayout = true
            stackFromEnd = true
        }
        recyclerView.adapter = adapter
    }

    private fun initPostsListener() {
        FirebaseDatabase.getInstance()
            .getReference("reports")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newReport = dataSnapshot.getValue<Report>(Report::class.java)
                    val reportID = dataSnapshot.key
                    if(newReport != null && reportID != null){
                        val postAndId = Pair(newReport, reportID.toString())
                        adapter.addReport(postAndId)
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    val removeable = dataSnapshot.getValue<Report>(Report::class.java)
                    val reportID = dataSnapshot.key
                    if(removeable != null && reportID != null){
                        val postAndId = Pair(removeable, reportID.toString())
                        adapter.removeReport(postAndId)
                    }
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
    }
}