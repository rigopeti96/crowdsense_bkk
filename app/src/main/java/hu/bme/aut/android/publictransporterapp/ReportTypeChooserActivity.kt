package hu.bme.aut.android.publictransporterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.publictransporterapp.adapter.ReportTypeAdapter
import hu.bme.aut.android.publictransporterapp.data.Station

class ReportTypeChooserActivity : AppCompatActivity() {

    /**
     * ReportItem fields:
     * reporttype
     * latitude
     * longitude
     * stationname
     * transport type (MUST add to the table)
     */

    var errorTypes: ArrayList<String> = ArrayList()
    val transportType: String = ""
    var stationName: String = ""
    var stopType: String = ""
    private lateinit var station: Station
    private var actualLat: Double = 0.0
    private var actualLong: Double = 0.0

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_type_chooser)
        val actpoz: Bundle? = intent.extras
        actualLat = actpoz!!.getDouble("actualLat")
        actualLong = actpoz.getDouble("actualLong")
        stationName = actpoz.getString("stationName", "")
        stopType = actpoz.getString("stoptype", "")
        station = Station(stationName, actualLat, actualLong, stopType)

        /**
         * The adding method MUST be refactored. This solution is temporaly!!!!
         */

        if(transportType != "BUS"){
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
            station
            )
        recyclerView.layoutManager  = LinearLayoutManager(this)
        recyclerView.adapter = customAdapter
    }
}