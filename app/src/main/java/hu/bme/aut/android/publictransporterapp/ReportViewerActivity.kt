package hu.bme.aut.android.publictransporterapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.gms.location.*
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.publictransporterapp.adapter.ReportAdapter
import hu.bme.aut.android.publictransporterapp.data.Report
import hu.bme.aut.android.publictransporterapp.data.ReportItem
import hu.bme.aut.android.publictransporterapp.data.ReportListDatabase
import kotlinx.android.synthetic.main.content_report.*
import java.time.LocalDateTime
import kotlin.concurrent.thread


class ReportViewerActivity: AppCompatActivity()/*, ReportAdapter.ReportItemClickListener*/ {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReportAdapter
    private lateinit var database: ReportListDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        database = Room.databaseBuilder(
            applicationContext,
            ReportListDatabase::class.java,
            "report-list2"
        ).build()
        initRecyclerView()
        initPostsListener()

    }

    /**
     * Database functions
     */

    private fun initRecyclerView() {
        recyclerView = ReportRecyclerView
        adapter = ReportAdapter(applicationContext)
        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            reverseLayout = true
            stackFromEnd = true
        }
        recyclerView.adapter = adapter

        /*
        * postsAdapter = PostsAdapter(applicationContext)
rvPosts.layoutManager = LinearLayoutManager(this).apply {
    reverseLayout = true
    stackFromEnd = true
}
rvPosts.adapter = postsAdapter
        * */
    }

    private fun initPostsListener() {
        FirebaseDatabase.getInstance()
            .getReference("reports")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newReport = dataSnapshot.getValue<Report>(Report::class.java)
                    adapter.addReport(newReport)
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
    }

    /*override fun onItemChanged(item: Report) {
        thread {
            database.reportItemDao().update(item)
        }
    }

    override fun deleteItem(item: Report) {
        thread{
            database.reportItemDao().deleteItem(item)
        }
    }*/
}