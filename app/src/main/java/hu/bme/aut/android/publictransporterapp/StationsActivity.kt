package hu.bme.aut.android.publictransporterapp

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

class StationsActivity : AppCompatActivity() {
    var StationName: ArrayList<String> = ArrayList()
    var latitude: ArrayList<String> = ArrayList()
    var longitude: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stations)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = linearLayoutManager
        try {
            val obj = JSONObject(loadJSONFromAsset())
            val stationArray = obj.getJSONArray("users")
            for (i in 0 until stationArray.length()) {
                val stationDetail = stationArray.getJSONObject(i)
                if(stationDetail.getString("name") == "Niyaz" || stationDetail.getString("name") == "Mahi"){
                    StationName.add(stationDetail.getString("name"))
                    latitude.add(stationDetail.getString("stop_lat"))
                    longitude.add(stationDetail.getString("stop_lon"))
                }
            }
        }
        catch (e: JSONException) {
            e.printStackTrace()
        }
        val customAdapter = StationAdapter(this@StationsActivity, StationName, latitude, longitude)
        recyclerView.adapter = customAdapter
    }

    private fun loadJSONFromAsset(): String {
        val json: String?
        try {
            val inputStream = assets.open("stops.json")
            Log.d("JSON file opened", "opened")
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
        Log.d("arrived", "arrived")
        return json
    }
}