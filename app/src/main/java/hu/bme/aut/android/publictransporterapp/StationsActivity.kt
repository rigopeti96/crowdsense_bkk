package hu.bme.aut.android.publictransporterapp

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.publictransporterapp.adapter.StationAdapter
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

/**
 * Must Fix!!!
 */

class StationsActivity : AppCompatActivity() {
    var StationName: ArrayList<String> = ArrayList()
    var latitude: ArrayList<String> = ArrayList()
    var longitude: ArrayList<String> = ArrayList()
    private var actualLat: Double = 0.0
    private var actualLong: Double = 0.0
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stations)
        val actpoz: Bundle? = intent.extras
        actualLat = actpoz!!.getDouble("actualLat")
        actualLong = actpoz!!.getDouble("actualLong")
        try {
            val obj = JSONObject(loadJSONFromAsset())
            val stationArray = obj.getJSONArray("stops")
            for (i in 0 until stationArray.length()) {
                val stationDetail = stationArray.getJSONObject(i)
                if(CalcDistance(stationDetail.getString("stop_lat").toDouble(),
                        stationDetail.getString("stop_lon").toDouble() ) <= 250){
                    StationName.add(stationDetail.getString("stop_name"))
                    latitude.add(stationDetail.getString("stop_lat"))
                    longitude.add(stationDetail.getString("stop_lon"))
                }
            }
        }
        catch (e: JSONException) {
            e.printStackTrace()
        }

        recyclerView = findViewById(R.id.recyclerView)
        val customAdapter = StationAdapter(this@StationsActivity, StationName, latitude, longitude)
        recyclerView.layoutManager  = LinearLayoutManager(this)
        recyclerView.adapter = customAdapter

    }

    private fun loadJSONFromAsset(): String {
        val json: String?
        try {
            val inputStream = assets.open("stops.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            val charset: Charset = Charsets.UTF_8
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, charset)
        }
        catch (ex: IOException) {
            Log.d("BAJ VAN", "NEM KICSI")
            ex.printStackTrace()
            return ""
        }
        return json
    }

    private fun CalcDistance(pointLat: Double, pointLong: Double) :Double{
        val distanceInMeter: Float
        val loc1 = Location("")
        loc1.latitude = pointLat
        loc1.longitude = pointLong

        val loc2 = Location("")
        loc2.latitude = actualLat
        loc2.longitude = actualLong

        distanceInMeter = loc1.distanceTo(loc2)

        return distanceInMeter.toDouble()
    }
}