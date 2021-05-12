package hu.bme.aut.android.publictransporterapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.publictransporterapp.adapter.ReportTypeAdapter
import hu.bme.aut.android.publictransporterapp.data.Station

class ReportTypeChooserActivity : AppCompatActivity() {

    var errorTypes: ArrayList<String> = ArrayList()
    var stationName: String = ""
    var stopType: String = ""
    private lateinit var station: Station
    private var actualLat: Double = 0.0
    private var actualLong: Double = 0.0
    private var reportTime: Float = 0F

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_type_chooser)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        reportTime = sharedPreferences.getFloat("time", 5F)
        val actpoz: Bundle? = intent.extras
        actualLat = actpoz!!.getDouble("actualLat")
        actualLong = actpoz.getDouble("actualLong")
        stationName = actpoz.getString("stationName", "")
        stopType = actpoz.getString("stoptype", "")
        station = Station(stationName, actualLat, actualLong, stopType)

        /**
         * The adding method MUST be refactored. This solution is temporaly!!!!
         */

        errorTypes.add(applicationContext.getString(R.string.placeholder))
        if(stopType == "TRAM" || stopType == "RAIL" || stopType == "TROLLEY"){
            errorTypes.add(applicationContext.getString(R.string.eletro_problem))
        }

        errorTypes.add(applicationContext.getString(R.string.traffic_problem_car))
        errorTypes.add(applicationContext.getString(R.string.traffic_problem_public))
        errorTypes.add(applicationContext.getString(R.string.conductor))
        errorTypes.add(applicationContext.getString(R.string.other))
        errorTypes.add(applicationContext.getString(R.string.traffic_jam))
        errorTypes.add(applicationContext.getString(R.string.weather))
        errorTypes.add(applicationContext.getString(R.string.unknown))
        errorTypes.add(applicationContext.getString(R.string.delay))
        errorTypes.add(applicationContext.getString(R.string.public_tile_error))
        errorTypes.add(applicationContext.getString(R.string.illness))

        /**
         * Create recyclerView
         */

        recyclerView = findViewById(R.id.recyclerView)
        val customAdapter = ReportTypeAdapter(this@ReportTypeChooserActivity,
            errorTypes,
            station,
            reportTime
            )
        recyclerView.layoutManager  = LinearLayoutManager(this)
        recyclerView.adapter = customAdapter
    }
}