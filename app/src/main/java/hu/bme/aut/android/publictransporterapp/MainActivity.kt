package hu.bme.aut.android.publictransporterapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
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

        btnCheckConductor.setOnClickListener{
            val problemIntent = Intent(this, ConductorActivity::class.java)
            startActivity(problemIntent)
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.exit_dialog_title)
        builder.setMessage(R.string.exit_dialog_message)

        builder.setPositiveButton(R.string.positive_button_text) { dialog, which ->
            finishAffinity()
        }

        builder.setNegativeButton(R.string.negative_button_text) { dialog, which ->
            dialog.dismiss()
        }

        btnExit.setOnClickListener{
            builder.show()
        }

    }
}