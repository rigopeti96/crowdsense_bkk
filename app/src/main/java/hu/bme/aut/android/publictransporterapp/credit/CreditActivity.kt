package hu.bme.aut.android.publictransporterapp.credit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.android.publictransporterapp.databinding.ActivityCreditBinding

class CreditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}