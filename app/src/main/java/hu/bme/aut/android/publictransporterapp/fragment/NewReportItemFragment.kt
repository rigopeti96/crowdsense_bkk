package hu.bme.aut.android.publictransporterapp.fragment

import android.app.Dialog
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.publictransporterapp.data.ReportItem
import hu.bme.aut.android.publictransporterapp.R
import hu.bme.aut.android.publictransporterapp.data.ReportType

class NewReportItemFragment(private val latitude: Double, private val longitude: Double): DialogFragment() {
    interface NewReportItemFragmentListener {
        fun onReportItemCreated(newItem: ReportItem)
    }

    private lateinit var reportTypeSpinner: Spinner
    private lateinit var listener: NewReportItemFragmentListener

    //private lateinit var reportdate: LocalDateTime
    override fun onAttach(context: Context) {
        super.onAttach(context)

        /**
         * MUST fix it
         */

        //val c = Calendar.getInstance()
        //val year = c.get(Calendar.YEAR)
        //val month = c.get(Calendar.MONTH)
        //val day = c.get(Calendar.DAY_OF_MONTH)
        //val hour = c.get(Calendar.HOUR_OF_DAY)
        //val minute = c.get(Calendar.MINUTE)
//
        //reportdate = LocalDateTime.of(year, month, day, hour, minute)

        listener = context as? NewReportItemFragmentListener
            ?: throw RuntimeException("Activity must implement the NewReportItemFragmentListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_errorrep)
            .setView(getContentView())
            .setPositiveButton(R.string.send_note) { dialogInterface, i ->
                if (isValid()) {
                    listener.onReportItemCreated(getReportItem())
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    companion object {
        const val TAG = "NewReportItemFragment"
    }

    private fun getContentView(): View {
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.new_report_item, null)

        /**
         * Conversion: <variable name> = contextView.findViewById(R.id.<ID in item_report_list.xml>)
         */

        //stationname = contentView.findViewById(R.id.stationname)
        reportTypeSpinner = contentView.findViewById(R.id.reporttype)
        reportTypeSpinner.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.reporttype)
            )
        )

        return contentView
    }

    private fun isValid() = true //titleEditText.text.isNotEmpty()

    private fun getReportItem() =
        ReportItem(
            id = null,
            reportType = ReportType.getByOrdinal(reportTypeSpinner.selectedItemPosition)
                ?: ReportType.TRAFFIC,
            latitude = latitude,
            longitude = longitude,
            /**
             * Station name have to be replaced by the correct name of the station.
             * The actual value is a placeholder
             */

            stationName = "Teszt utca megálló"
            //reportDate = reportdate
        )
}