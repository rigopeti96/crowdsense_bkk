package hu.bme.aut.android.publictransporterapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.publictransporterapp.adapter.StationAdapter
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class StationsActivity : AppCompatActivity() {
    var personName: ArrayList<String> = ArrayList()
    var emailId: ArrayList<String> = ArrayList()
    var mobileNumbers: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = linearLayoutManager
        try {
            val obj = JSONObject(loadJSONFromAsset())
            val userArray = obj.getJSONArray("users")
            for (i in 0 until userArray.length()) {
                val userDetail = userArray.getJSONObject(i)
                if(userDetail.getString("name") == "Niyaz" || userDetail.getString("name") == "Mahi"){
                    personName.add(userDetail.getString("name"))
                    emailId.add(userDetail.getString("email"))
                    val contact = userDetail.getJSONObject("contact")
                    mobileNumbers.add(contact.getString("mobile"))
                }
            }
        }
        catch (e: JSONException) {
            e.printStackTrace()
        }
        val customAdapter = StationAdapter(this@StationsActivity, personName, emailId, mobileNumbers)
        recyclerView.adapter = customAdapter
    }
    private fun loadJSONFromAsset(): String {
        val json: String?
        try {
            val inputStream = assets.open("stops.json")
            Log.d("JSON file opened", "opened")
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
}