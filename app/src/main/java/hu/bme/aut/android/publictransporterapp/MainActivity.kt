package hu.bme.aut.android.publictransporterapp

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    /**
     * To testing leave the function of write out the actual position but it is deletable from the final release
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(2000)
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSendProblem.setOnClickListener {
            val trafficIntent = Intent(this, ReportActivity::class.java)
            startActivity(trafficIntent)
        }

        btnCheckProblem.setOnClickListener{
            val problemIntent = Intent(this, TrafficErrorActivity::class.java)
            startActivity(problemIntent)
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.exit_dialog_title)
        builder.setMessage(R.string.exit_dialog_message)
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(R.string.positive_button_text) { dialog, which ->
            finishAffinity();
            exitProcess(0);
        }

        builder.setNegativeButton(R.string.negative_button_text) { dialog, which ->
            dialog.dismiss()
        }

        btnExit.setOnClickListener{
            builder.show()
        }

    }
}