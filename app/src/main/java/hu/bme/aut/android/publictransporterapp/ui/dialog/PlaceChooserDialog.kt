package hu.bme.aut.android.publictransporterapp.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.publictransporterapp.R
import hu.bme.aut.android.publictransporterapp.ReportTypeChooserActivity
import hu.bme.aut.android.publictransporterapp.StationPickerActivity
import hu.bme.aut.android.publictransporterapp.data.TransportType
import kotlinx.android.synthetic.main.dialog_place_chooser.*

class PlaceChooserDialog(val lat: Double, val lon: Double): DialogFragment() {
    private var radioGroupValue: Int = 0
    private lateinit var SpinnerTransportType: Spinner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_place_chooser, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SpinnerTransportType = view.findViewById(R.id.transportTypeSpinner)
        SpinnerTransportType.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.transportTypes)
        )
        SpinnerTransportType.isEnabled = false

        val sendBtn: Button = view.findViewById(R.id.btnSend)
        sendBtn.isEnabled = false

        val radioGroup = view.findViewById<RadioGroup>(R.id.rgTypeSelector)
        val radButton1: Button
        radButton1 = RadioButton(context)
        radButton1.text = "Megállóban"
        radioGroup.addView(radButton1)

        val radButton2: Button
        radButton2 = RadioButton(context)
        radButton2.text = "Útközben"
        radioGroup.addView(radButton2)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            checkRadioGroupValue(checkedId, sendBtn)
        }

        sendBtn.setOnClickListener {
            if(radioGroupValue%2 == 1){
                val trafficIntent = Intent(context, StationPickerActivity::class.java)
                trafficIntent.putExtra("actualLat", lat)
                trafficIntent.putExtra("actualLong", lon)
                startActivity(trafficIntent)
            } else {
                val reportTypeIntent = Intent(context, ReportTypeChooserActivity::class.java)
                reportTypeIntent.putExtra("actualLat", lat)
                reportTypeIntent.putExtra("actualLong", lon)
                reportTypeIntent.putExtra("stationName", "NOSTATION")
                reportTypeIntent.putExtra("stoptype",
                    TransportType.getByOrdinal(SpinnerTransportType.selectedItemPosition).toString())
                startActivity(reportTypeIntent)
            }
        }
    }

    private fun checkRadioGroupValue(checkedId: Int, sendBtn: Button){
        radioGroupValue = checkedId
        sendBtn.isEnabled = true
        transportTypeSpinner.isEnabled = checkedId % 2 == 0
        Log.d("Is changed?", "Yes")
        Log.d("radioGroupValue", radioGroupValue.toString())
    }
}