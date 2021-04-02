package hu.bme.aut.android.publictransporterapp.ui.dialog

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.publictransporterapp.R
import kotlinx.android.synthetic.main.location_alert_dialog.*

class LocationAlertDialog : DialogFragment() {
    private val SHARED_PREFS: String = "sharedPrefs"
    private val ACCEPT: String = "accept"

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
        btnAccept.isEnabled = cbAccept.isChecked

        cbAccept.setOnCheckedChangeListener { buttonView, isChecked ->
            btnAccept.isEnabled = isChecked
        }

        btnAccept.setOnClickListener {
            saveSettings(cbAccept.isChecked)
            dismiss()
        }
    }

    private fun saveSettings(isAccepted: Boolean){
        val sharedPreferences = activity?.getSharedPreferences(SHARED_PREFS,
            AppCompatActivity.MODE_PRIVATE
        )

        val editor: SharedPreferences.Editor = sharedPreferences?.edit() ?: return
        editor.putBoolean(ACCEPT, isAccepted)
        editor.apply()
    }
}