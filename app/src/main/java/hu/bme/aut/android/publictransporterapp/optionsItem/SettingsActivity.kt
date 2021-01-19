package hu.bme.aut.android.publictransporterapp.optionsItem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import hu.bme.aut.android.publictransporterapp.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        radiusSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
            }
        })

        radiusSlider.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
        }

        timeSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
            }
        })

        timeSlider.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
        }
    }
}
