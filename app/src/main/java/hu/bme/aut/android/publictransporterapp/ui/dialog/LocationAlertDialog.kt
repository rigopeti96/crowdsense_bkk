package hu.bme.aut.android.publictransporterapp.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.publictransporterapp.R

class LocationAlertDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.location_alert_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cbAccept = view.findViewById<CheckBox>(R.id.cbAccept)
        val btnAccept = view.findViewById<Button>(R.id.btnAccept)
        cbAccept.setOnCheckedChangeListener { buttonView, isChecked ->
            btnAccept.isEnabled = isChecked
        }
    }
}