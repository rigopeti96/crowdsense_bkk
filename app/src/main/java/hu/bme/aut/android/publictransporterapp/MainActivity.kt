package hu.bme.aut.android.publictransporterapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import at.markushi.ui.CircleButton
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.publictransporterapp.credit.CreditActivity
import hu.bme.aut.android.publictransporterapp.data.Report
import hu.bme.aut.android.publictransporterapp.optionsItem.SettingsActivity
import hu.bme.aut.android.publictransporterapp.service.LocationService
import hu.bme.aut.android.publictransporterapp.ui.dialog.PlaceChooserDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.util.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var locationService: LocationService
    private val PERMISSION_ID = 1010
    var location: Location = Location("")
    private val reportList = mutableListOf<Pair<Report, String>>()
    private var actualSearchRange = 50F
    private var moves: Int = 0

    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("actual lat", location.latitude.toString())
        Log.d("actual long", location.longitude.toString())
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        actualSearchRange = sharedPreferences.getFloat("range", 50F)
        setupLocationService()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val actReactString: String = actualSearchRange.toInt().toString() + " " + applicationContext.getString(
            R.string.meter
        )
        actSearchDist.text = actReactString

        btnSendProblem.setOnClickListener {
            showDialogFragment()
        }
        initPostsListener()
    }

    private fun showDialogFragment(){
        val fm: FragmentManager = supportFragmentManager
        val placeChooser = PlaceChooserDialog(location.latitude, location.longitude)
        placeChooser.show(fm, "dialog_place_chooser")
    }

    private fun setupLocationService() {
        locationService = LocationService(this, {
            location = it.lastLocation
            if (location.latitude == 0.0 || location.longitude == 0.0) {
                locationService.requestCurrentLocation()
            } else {
                yourpose.text = getCompleteAddressString(location.latitude, location.longitude)
                mMap?.let { onMapReady(it) }
            }
        }, PERMISSION_ID)
        if (locationService.requestNecessaryPermissions()) {
            locationService.requestCurrentLocation()
            locationService.setupPeriodicLocationRequest()
        }
    }

    override fun onRestart() {
        super.onRestart()
        setupLocationService()
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        actualSearchRange = sharedPreferences.getFloat("range", 50F)

        val actReactString: String = actualSearchRange.toInt().toString() + " " + applicationContext.getString(
            R.string.meter
        )
        actSearchDist.text = actReactString

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
                val creditIntent = Intent(this, CreditActivity::class.java)
                startActivity(creditIntent)
                true
            }
            R.id.reports -> {
                val reportIntent = Intent(this, ReportViewerActivity::class.java)
                startActivity(reportIntent)
                true
            }
            R.id.options -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initPostsListener() {
        FirebaseDatabase.getInstance()
            .getReference("reports")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val reportItemData = dataSnapshot.getValue<Report>(Report::class.java)
                    val reportItemKey = dataSnapshot.key
                    if (reportItemData != null && reportItemKey != null) {
                        val postAndId = Pair(reportItemData, reportItemKey.toString())
                        reportList.add(postAndId)
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    for (i in 0 until reportList.size) {
                        if (reportList[i].first.reportDate!! > getTodayAsString().toString()) {
                            Log.d("Found?", "Found!")
                            FirebaseDatabase.getInstance().reference.child("reports").child(reportList[i].second).removeValue()
                        }
                    }
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
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
            } else {
                Log.w("My Current loction address", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("My Current loction address", "Cannot get Address!")
        }
        return strAdd
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                locationService.requestCurrentLocation()
                locationService.setupPeriodicLocationRequest()
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
        val circleButton: CircleButton = findViewById(R.id.btnCenter)

        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
        googleMap.setMapStyle(mapStyleOptions)
        mMap?.clear()
        mMap?.addMarker(MarkerOptions().position(yourLocation).title("Aktuális Pozíció"))
        for(i in reportList.indices){
            if(reportList[i].first.reportDateUntil!! > getTodayAsString().toString()){
                val errorLatLng = LatLng(
                    reportList[i].first.latitude!!,
                    reportList[i].first.longitude!!
                )
                val errorTypeWithLocation: String = reportList[i].first.reportType + ", " + reportList[i].first.stationName
                when(reportList[i].first.transportType){
                    "BUS" -> mMap?.addMarker(
                        MarkerOptions()
                            .position(errorLatLng)
                            .title(errorTypeWithLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    )
                    "TROLLEY" -> mMap?.addMarker(
                        MarkerOptions()
                            .position(errorLatLng)
                            .title(errorTypeWithLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    )
                    "M2" -> mMap?.addMarker(
                        MarkerOptions()
                            .position(errorLatLng)
                            .title(errorTypeWithLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    )
                    "TRAM" -> mMap?.addMarker(
                        MarkerOptions()
                            .position(errorLatLng)
                            .title(errorTypeWithLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                    )
                    "M1" -> mMap?.addMarker(
                        MarkerOptions()
                            .position(errorLatLng)
                            .title(errorTypeWithLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                    )
                    "RAIL" -> mMap?.addMarker(
                        MarkerOptions()
                            .position(errorLatLng)
                            .title(errorTypeWithLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                    )
                    "M3" -> mMap?.addMarker(
                        MarkerOptions()
                            .position(errorLatLng)
                            .title(errorTypeWithLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    )
                    "M4" -> mMap?.addMarker(
                        MarkerOptions()
                            .position(errorLatLng)
                            .title(errorTypeWithLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    )
                    "NIGHTBUS" -> mMap?.addMarker(
                        MarkerOptions()
                            .position(errorLatLng)
                            .title(errorTypeWithLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                    )
                    else -> mMap?.addMarker(
                        MarkerOptions()
                            .position(errorLatLng)
                            .title(errorTypeWithLocation)
                    )
                }
            }
        }

        if(moves < 2){
            cameraToCenter(yourLocation)
            moves++
        }

        circleButton.setOnClickListener {
            cameraToCenter(yourLocation)
        }
    }

    private fun cameraToCenter(yourLocation: LatLng){
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(yourLocation))
        mMap?.animateCamera(CameraUpdateFactory.zoomTo(15F))
    }

    private fun getTodayAsString(): LocalDateTime {
        return LocalDateTime.now()
    }
}