package hu.bme.aut.android.publictransporterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.publictransporterapp.adapter.ReportTypeAdapter
import hu.bme.aut.android.publictransporterapp.adapter.StationAdapter
import kotlinx.android.synthetic.main.activity_report_type_chooser.*

class ReportTypeChooserActivity : AppCompatActivity() {
    var errorTypes: ArrayList<String> = ArrayList()
    val transportType: String = ""
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_type_chooser)

        /**
         * The adding method MUST be refactored. This solution is temporaly!!!!
         */

        if(transportType != "BUS"){
            errorTypes.add(R.string.eletro_problem.toString())
        }
        errorTypes.add(R.string.traffic_problem_car.toString())
        errorTypes.add(R.string.traffic_problem_public.toString())
        errorTypes.add(R.string.conductor.toString())
        errorTypes.add(R.string.other.toString())
        errorTypes.add(R.string.traffic_jam.toString())
        errorTypes.add(R.string.weather.toString())
        errorTypes.add(R.string.unknown.toString())
        errorTypes.add(R.string.delay.toString())
        errorTypes.add(R.string.public_tile_error.toString())
        errorTypes.add(R.string.illness.toString())

        recyclerView = findViewById(R.id.recyclerView)
        val customAdapter = ReportTypeAdapter(this@ReportTypeChooserActivity, errorTypes)
        recyclerView.layoutManager  = LinearLayoutManager(this)
        recyclerView.adapter = customAdapter
    }

}