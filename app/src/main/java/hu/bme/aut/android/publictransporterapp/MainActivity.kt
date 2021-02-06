package hu.bme.aut.android.publictransporterapp

import BackgroundLocationService
import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hu.bme.aut.android.publictransporterapp.data.ReportItem
import hu.bme.aut.android.publictransporterapp.data.ReportListDatabase
import hu.bme.aut.android.publictransporterapp.optionsItem.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val PERMISSION_ID = 1010
    var location: Location = Location("")
    private lateinit var database: ReportListDatabase
    private lateinit var reportList: List<ReportItem>
    private var actualSearchRange = 50F

    private var gpsService: BackgroundLocationService? = null

    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(2000)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        newLocationData()
        Log.d("actual lat", location.latitude.toString())
        Log.d("actual long", location.longitude.toString())
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        actualSearchRange = sharedPreferences.getFloat("range", 50F)

        database = Room.databaseBuilder(
            applicationContext,
            ReportListDatabase::class.java,
            "report-list2"
        ).build()
        loadItemsInBackground()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val actReactString: String = actualSearchRange.toInt().toString() + " " + applicationContext.getString(R.string.meter)
        actSearchDist.text = actReactString

        btnSendProblem.setOnClickListener {
            val trafficIntent = Intent(this, StationPickerActivity::class.java)
            trafficIntent.putExtra("actualLat", location.latitude)
            trafficIntent.putExtra("actualLong", location.longitude)
            startActivity(trafficIntent)
        }

        val serviceIntent = Intent(this.application, BackgroundLocationService::class.java)
        this.application.startService(serviceIntent)
        this.application.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onRestart() {
        super.onRestart()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        newLocationData()
        Log.d("actual lat", location.latitude.toString())
        Log.d("actual long", location.longitude.toString())

        loadItemsInBackground()
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        actualSearchRange = sharedPreferences.getFloat("range", 50F)

        val actReactString: String = actualSearchRange.toInt().toString() + " " + applicationContext.getString(R.string.meter)
        actSearchDist.text = actReactString

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        gpsService?.stopTracking()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.quit -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.exit_dialog_title)
                builder.setMessage(R.string.exit_dialog_message)

                builder.setPositiveButton(R.string.positive_button_text) { dialog, which ->
                    finishAffinity()
                }

                builder.setNegativeButton(R.string.negative_button_text) { dialog, which ->
                    dialog.dismiss()
                }
                builder.show()
                true
            }
            R.id.credit -> {
                true
            }
            R.id.reports -> {
                val reportIntent = Intent(this, ReportViewerActivity::class.java)
                startActivity(reportIntent)
                true
            }
            R.id.options ->{
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadItemsInBackground() {
        thread {
            reportList = database.reportItemDao().getAll()
        }
    }

    /**
     * Get the actual street based on position
     */
    private fun getCompleteAddressString(
        LATITUDE: Double,
        LONGITUDE: Double
    ): String? {
        var strAdd = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses =
                geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
                Log.w("My Current loction address", strReturnedAddress.toString())
            } else {
                Log.w("My Current loction address", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("My Current loction address", "Cannot get Address!")
        }
        return strAdd
    }

    /**
     * Get location functions
     */

    private fun getLastLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task->
                    location = task.result
                    Log.d("Value after ...=task.result", location.longitude.toString())
                    Log.d("Value after ...=task.result", location.latitude.toString())
                    if(location.latitude == 0.0 || location.longitude == 0.0){
                        newLocationData()
                    }else{
                        yourpose.text = getCompleteAddressString(location.latitude, location.longitude)
                        mMap?.let { onMapReady(it) }
                        location = task.result
                        Log.d("Debug:" ,"Your Location:"+ location.longitude)
                    }
                }
            } else {
                Toast.makeText(this,"Please Turn on Your device Location", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermission()
        }
    }

    private fun newLocationData(){
        val locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation = locationResult.lastLocation
            Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
        }
    }

    private fun checkPermission():Boolean{
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if(
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
        return false

    }

    private fun requestPermission(){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled():Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug:","You have the Permission")
            }
        }
    }

    private val serviceConnection = object: ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            if (name?.className.equals("BackgroundLocationService")) {
                gpsService = null
            }
        }

        override fun onServiceConnected(className:ComponentName, service:IBinder) {
            val name = className.className
            if (name.endsWith("BackgroundLocationService"))
            {
                gpsService = (service as BackgroundLocationService.LocationServiceBinder).service
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val yourLocation = LatLng(location.latitude, location.longitude)
        mMap?.clear()
        mMap?.addMarker(MarkerOptions().position(yourLocation).title("Aktuális pozíció"))
        for(i in reportList.indices){
            val errorLatLng = LatLng(reportList[i].latitude, reportList[i].longitude)
            val errorTypeWithLocation: String = reportList[i].reportType + ", " + reportList[i].stationName
            if(reportList[i].transportType == "BUS"){
                mMap?.addMarker(MarkerOptions()
                    .position(errorLatLng)
                    .title(errorTypeWithLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
            } else if(reportList[i].transportType == "TROLLEY" || reportList[i].transportType == "M2"){
                mMap?.addMarker(MarkerOptions()
                    .position(errorLatLng)
                    .title(errorTypeWithLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
            } else if(reportList[i].transportType == "TRAM" || reportList[i].transportType == "M1"){
                mMap?.addMarker(MarkerOptions()
                    .position(errorLatLng)
                    .title(errorTypeWithLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
            } else if(reportList[i].transportType == "RAIL"){
                mMap?.addMarker(MarkerOptions()
                    .position(errorLatLng)
                    .title(errorTypeWithLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)))
            } else if(reportList[i].transportType == "M3"){
                mMap?.addMarker(MarkerOptions()
                    .position(errorLatLng)
                    .title(errorTypeWithLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
            } else if(reportList[i].transportType == "M4"){
                mMap?.addMarker(MarkerOptions()
                    .position(errorLatLng)
                    .title(errorTypeWithLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
            } else if(reportList[i].transportType == "NIGHTBUS"){
                mMap?.addMarker(MarkerOptions()
                    .position(errorLatLng)
                    .title(errorTypeWithLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
            } else {
                mMap?.addMarker(MarkerOptions()
                    .position(errorLatLng)
                    .title(errorTypeWithLocation))
            }
        }
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(yourLocation))
        mMap?.animateCamera(CameraUpdateFactory.zoomTo(15F))
    }
}