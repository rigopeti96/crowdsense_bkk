package hu.bme.aut.android.publictransporterapp.adapter

import android.content.Context
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.publictransporterapp.R
import hu.bme.aut.android.publictransporterapp.data.Report
import hu.bme.aut.android.publictransporterapp.data.ReportItem
import hu.bme.aut.android.publictransporterapp.data.Station
import kotlinx.android.synthetic.main.item_report_list.view.*
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import java.io.InputStream


class ReportAdapter(/*private val listener: ReportItemClickListener,*/ val context: Context):
    RecyclerView.Adapter<ReportAdapter.ReportItemViewHolder>() {

    /*private val stationsAll: ArrayList<Station> = ArrayList()
    private val stationTypes: ArrayList<String> = ArrayList()
    private var closestDistance: Int = 1000000000*/

    private val items = mutableListOf<Report>()
    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportItemViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_report_list, parent, false)
        return ReportItemViewHolder(itemView)
    }

    /*interface ReportItemClickListener {
        fun onItemChanged(item: Report)
        fun deleteItem(item: Report)
    }*/

    inner class ReportItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val reportType: TextView
        /*val latitude: TextView
        val longitude: TextView*/
        val stationName: TextView
        val stationType: TextView
        /*val removeBtn: ImageButton*/

        var item: Report? = null

        /**
         * Conversion:
         * <variable name> = itemView.findViewById(R.id.<ID of the item, could be find in item_report_list.xml>)
         */

        init {
            reportType = itemView.findViewById(R.id.reporttype)
            stationName = itemView.findViewById(R.id.stationname)
            stationType = itemView.findViewById(R.id.stationType)
            /*removeBtn = itemView.findViewById(R.id.removebtn)
            removeBtn.setOnClickListener{
                if(item != null){
                    items.remove(item!!)
                    listener.deleteItem(item!!)
                    notifyDataSetChanged()
                }
            }*/
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ReportItemViewHolder, position: Int) {
        val item = items[position]

        /**
         * Conversion
         * holder.<name in class ReportItemViewHolder.text = item.<name in ReportItem>[.toString()]>
         */

        holder.reportType.text = item.reportType
        holder.stationType.text = item.transportType
        if(item.stationName != "NOSTATION"){
            holder.stationName.text = item.stationName
        } else {
            holder.stationName.text = "ÚTKÖZBEN"
        }

        holder.item = item
    }

    /*fun update(reportItems: List<ReportItem>) {
        items.clear()
        items.addAll(reportItems)
        notifyDataSetChanged()
    }*/

    fun addReport(reportItem: Report?) {
        reportItem ?: return

        items.add(reportItem)
        notifyDataSetChanged()
    }

    /*private fun findTheNearestStation(itemType: String){
        try {
            val obj = JSONObject(loadJSONFromAsset())
            val stationArray = obj.getJSONArray("stops")
            for (i in 0 until stationArray.length()) {
                val stationDetail = stationArray.getJSONObject(i)
                if(CalcDistance(stationDetail.getString("lat").toDouble(),
                        stationDetail.getString("lon").toDouble() ) <= closestDistance.toDouble() &&
                    stationDetail.getString("stopColorType") == itemType){
                    if(stationsAll.size > 1){
                        var duplicateCounter: Int = 0
                        for(j in 0 until stationsAll.size){
                            if(stationDetail.getString("name") == stationsAll[j].name
                                && stationDetail.getString("stopColorType") == stationsAll[j].stopType) {
                                duplicateCounter++
                            }
                        }
                        if(duplicateCounter == 0){
                            val latitude: Double = stationDetail.getDouble("lat")
                            val longitude: Double = stationDetail.getDouble("lon")
                            val name: String = stationDetail.getString("name")
                            val stationType = if(stationDetail.getString("stopColorType") == "H5"
                                || stationDetail.getString("stopColorType") == "H6"
                                || stationDetail.getString("stopColorType") == "H7"
                                || stationDetail.getString("stopColorType") == "H8"
                                || stationDetail.getString("stopColorType") == "H9"){
                                "RAIL"
                            } else {
                                stationDetail.getString("stopColorType")
                            }
                            stationsAll.add(Station(name, latitude, longitude, stationType))
                        }
                    } else {
                        val latitude: Double = stationDetail.getDouble("lat")
                        val longitude: Double = stationDetail.getDouble("lon")
                        val name: String = stationDetail.getString("name")
                        val stationType = if (stationDetail.getString("stopColorType") == "H5"
                            || stationDetail.getString("stopColorType") == "H6"
                            || stationDetail.getString("stopColorType") == "H7"
                            || stationDetail.getString("stopColorType") == "H8"
                            || stationDetail.getString("stopColorType") == "H9"
                        ) {
                            "RAIL"
                        } else {
                            stationDetail.getString("stopColorType")
                        }
                        stationsAll.add(Station(name, latitude, longitude, stationType))
                    }
                }
            }
        } catch (e: IOException){
            Log.d("IO Exception", e.toString())
        }
    }

    private fun loadJSONFromAsset(): String {
        val json: String?
        try {
            //val inputStream: InputStream = assets.open("stops.json")
            val inputStream = assets
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
        loc2.latitude =
        loc2.longitude = actualLong

        distanceInMeter = loc1.distanceTo(loc2)

        return distanceInMeter.toDouble()
    }*/
}