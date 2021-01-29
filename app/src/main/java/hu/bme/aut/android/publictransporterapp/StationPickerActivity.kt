package hu.bme.aut.android.publictransporterapp

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.publictransporterapp.adapter.StationAdapter
import hu.bme.aut.android.publictransporterapp.data.Station
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class StationPickerActivity : AppCompatActivity() {

    private val stationsAll: ArrayList<Station> = ArrayList()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var recyclerView: RecyclerView
    private var actualLat: Double = 0.0
    private var actualLong: Double = 0.0
    private var searchInRange: Float = 50F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station_picker)
        recyclerView = findViewById(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = linearLayoutManager
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actpoz: Bundle? = intent.extras
        actualLat = actpoz!!.getDouble("actualLat")
        actualLong = actpoz.getDouble("actualLong")
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        searchInRange = sharedPreferences.getFloat("range", 50F)

        /**
         * Read JSON
         */
        try {
            val obj = JSONObject(loadJSONFromAsset())
            val stationArray = obj.getJSONArray("stops")
            for (i in 0 until stationArray.length()) {
                val stationDetail = stationArray.getJSONObject(i)
                if(CalcDistance(stationDetail.getString("lat").toDouble(),
                        stationDetail.getString("lon").toDouble() ) <= searchInRange.toDouble()){
                    val latitude: Double = stationDetail.getDouble("lat")
                    val longitude: Double = stationDetail.getDouble("lon")
                    val name: String = stationDetail.getString("name")
                    val stationType: String = stationDetail.getString("stopColorType")
                    stationsAll.add(Station(name, latitude, longitude, stationType))
                }
            }
        }
        catch (e: JSONException) {
            e.printStackTrace()
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val menuItemBus: MenuItem = navView.menu.findItem(R.id.checkboxBus)
        val menuItemTram: MenuItem = navView.menu.findItem(R.id.checkboxTram)
        val menuItemRail: MenuItem = navView.menu.findItem(R.id.checkboxRail)
        val menuItemTrolley: MenuItem = navView.menu.findItem(R.id.checkboxTrolley)
        val menuItemMetroM1: MenuItem = navView.menu.findItem(R.id.checkboxM1)
        val menuItemMetroM2: MenuItem = navView.menu.findItem(R.id.checkboxM2)
        val menuItemMetroM3: MenuItem = navView.menu.findItem(R.id.checkboxM3)
        val menuItemMetroM4: MenuItem = navView.menu.findItem(R.id.checkboxM4)
        val menuItemNBus: MenuItem = navView.menu.findItem(R.id.checkboxNBus)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        (menuItemBus.actionView as CheckBox).setOnCheckedChangeListener { buttonView, isChecked ->
            updateAdapter(isChecked, "BUS")
        }

        (menuItemTram.actionView as CheckBox).setOnCheckedChangeListener { buttonView, isChecked ->
            updateAdapter(isChecked, "TRAM")
        }

        (menuItemRail.actionView as CheckBox).setOnCheckedChangeListener { buttonView, isChecked ->
            updateAdapter(isChecked, "RAIL")
        }

        (menuItemTrolley.actionView as CheckBox).setOnCheckedChangeListener { buttonView, isChecked ->
            updateAdapter(isChecked, "TROLLEY")
        }

        (menuItemMetroM1.actionView as CheckBox).setOnCheckedChangeListener { buttonView, isChecked ->
            updateAdapter(isChecked, "M1")
        }

        (menuItemMetroM2.actionView as CheckBox).setOnCheckedChangeListener { buttonView, isChecked ->
            updateAdapter(isChecked, "M2")
        }

        (menuItemMetroM3.actionView as CheckBox).setOnCheckedChangeListener { buttonView, isChecked ->
            updateAdapter(isChecked, "M3")
        }

        (menuItemMetroM4.actionView as CheckBox).setOnCheckedChangeListener { buttonView, isChecked ->
            updateAdapter(isChecked, "M4")
        }

        (menuItemNBus.actionView as CheckBox).setOnCheckedChangeListener { buttonView, isChecked ->
            updateAdapter(isChecked, "NIGHTBUS")
        }

        updateAdapter((menuItemBus.actionView as CheckBox).isChecked, "")

    }

    private fun updateAdapter(isChecked: Boolean = false, transportType: String) {
        val list: ArrayList<Station> = ArrayList()
        if (isChecked) {
            for (i in 0 until stationsAll.size){
                if(stationsAll[i].stopType == transportType){
                    list.add(stationsAll[i])
                }
            }
        } else {
            list.addAll(stationsAll)
        }
        recyclerView.adapter = StationAdapter(this@StationPickerActivity, list)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.station_picker, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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
        Log.d("arrived", "arrived")
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