package hu.bme.aut.android.publictransporterapp.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.publictransporterapp.R
import hu.bme.aut.android.publictransporterapp.StationPickerActivity

class PlaceChooserDialog(val lat: Double, val lon: Double): DialogFragment() {

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
        val btnState: Button = view.findViewById(R.id.instation)
        btnState.setOnClickListener {
            val trafficIntent = Intent(context, StationPickerActivity::class.java)
            trafficIntent.putExtra("actualLat", lat)
            trafficIntent.putExtra("actualLong", lon)
            startActivity(trafficIntent)
        }
    }
}