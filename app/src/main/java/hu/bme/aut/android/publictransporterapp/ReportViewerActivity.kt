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
import hu.bme.aut.android.publictransporterapp.adapter.ReportAdapter
import hu.bme.aut.android.publictransporterapp.data.ReportItem
import hu.bme.aut.android.publictransporterapp.data.ReportListDatabase
import kotlinx.android.synthetic.main.content_report.*
import kotlin.concurrent.thread


class ReportViewerActivity: AppCompatActivity(), ReportAdapter.ReportItemClickListener {

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

    }

    /**
     * Database functions
     */

    private fun initRecyclerView() {
        recyclerView = ReportRecyclerView
        adapter = ReportAdapter(this)
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.reportItemDao().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onItemChanged(item: ReportItem) {
        thread {
            database.reportItemDao().update(item)
        }
    }

    override fun deleteItem(item: ReportItem) {
        thread{
            database.reportItemDao().deleteItem(item)
        }
    }
}