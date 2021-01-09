package hu.bme.aut.android.publictransporterapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import hu.bme.aut.android.publictransporterapp.adapter.ReportAdapter
import hu.bme.aut.android.publictransporterapp.data.ReportItem
import hu.bme.aut.android.publictransporterapp.data.ReportListDatabase
import hu.bme.aut.android.publictransporterapp.data.ReportType
import kotlinx.android.synthetic.main.content_report.*
import kotlin.concurrent.thread

class TrafficErrorActivity : AppCompatActivity(), ReportAdapter.ReportItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReportAdapter
    private lateinit var database: ReportListDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic_error)
        setSupportActionBar(findViewById(R.id.toolbar))

        database = Room.databaseBuilder(
            applicationContext,
            ReportListDatabase::class.java,
            "traffic-error-list"
        ).build()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = ReportRecyclerView
        adapter = ReportAdapter(this)
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.reportItemDao().getTrafOrCond(ReportType.TRAFFIC)
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