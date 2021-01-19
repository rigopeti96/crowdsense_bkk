package hu.bme.aut.android.publictransporterapp.optionsItem

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.slider.Slider
import hu.bme.aut.android.publictransporterapp.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    var rangeValue: Float = 50F
    var timeValue: Float = 5F

    /**
     * Variables to Shared Preferences
     */
    private val SHARED_PREFS: String = "sharedPrefs"
    private val RANGE: String = "range"
    private val TIME: String = "time"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        loadSettings()
        radiusSlider.value = rangeValue
        timeSlider.value = timeValue
        /**
         * radiusSlider slider (1st) listeners.
         */

        radiusSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                Log.d("Touch start value", radiusSlider.value.toString())
            }

            override fun onStopTrackingTouch(slider: Slider) {
                val actualRange = radiusSlider.value
                val actualTime = timeSlider.value
                saveSettings(actualRange, actualTime)
            }
        })

        radiusSlider.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
        }

        /**
         * timeSlider slider (2nd) listeners.
         */

        timeSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: Slider) {
                val actualRange = radiusSlider.value
                val actualTime = timeSlider.value
                saveSettings(actualRange, actualTime)
            }
        })

        timeSlider.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
        }
    }

    /**
     * Functions to use of Shared Preferences
     *
     * private val SHARED_PREFS: String = "sharedPrefs"
       private val RANGE: String = "range"
       private val TIME: String = "time"
     */

    private fun saveSettings(range: Float, time: Float){
        val sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.putFloat(RANGE, range)
        editor.putFloat(TIME, time)

        editor.apply()
    }

    private fun loadSettings(){
        val sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        rangeValue = sharedPreferences.getFloat(RANGE, 50F)
        timeValue = sharedPreferences.getFloat(TIME, 5F)
    }
}
