package hu.bme.aut.android.publictransporterapp

import android.os.Bundle
import android.view.Menu
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import hu.bme.aut.android.publictransporterapp.adapter.ReportAdapter
import hu.bme.aut.android.publictransporterapp.data.ReportItem
import hu.bme.aut.android.publictransporterapp.data.ReportListDatabase
import hu.bme.aut.android.publictransporterapp.fragment.NewReportItemFragment
import kotlinx.android.synthetic.main.activity_report.*
import kotlinx.android.synthetic.main.content_report.*
import kotlin.concurrent.thread


class ReportActivity : AppCompatActivity(), ReportAdapter.ReportItemClickListener, NewReportItemFragment.NewReportItemFragmentListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReportAdapter
    private lateinit var database: ReportListDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        setSupportActionBar(findViewById(R.id.toolbar))

        fab.setOnClickListener{
            NewReportItemFragment().show(
                supportFragmentManager,
                NewReportItemFragment.TAG
            )
        }

        database = Room.databaseBuilder(
            applicationContext,
            ReportListDatabase::class.java,
            "match-list"
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

    override fun onReportItemCreated(newItem: ReportItem) {
        thread {
            val newId = database.reportItemDao().insert(newItem)
            val newReportItem = newItem.copy(
                id = newId
            )
            runOnUiThread {
                adapter.addItem(newReportItem)
            }
        }
    }
}