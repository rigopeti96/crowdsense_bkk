package hu.bme.aut.android.publictransporterapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import hu.bme.aut.android.publictransporterapp.ui.dialog.LocationAlertDialog
import kotlinx.android.synthetic.main.content_login.*

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private var progressDialog: ProgressDialog? = null
    private val SHARED_PREFS: String = "sharedPrefs"
    private var acceptedTerms: Boolean = false
    private val ACCEPT: String = "accept"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.toolbar))

        loadSettings()

        if(!acceptedTerms){
            showAlertFirstStart()
        }

        btnRegister.setOnClickListener {
            if(!acceptedTerms){
                showAlertFirstStart()
            }
            registerClick()
        }
        btnLogin.setOnClickListener {
            if(!acceptedTerms){
                showAlertFirstStart()
            }
            loginClick()
        }

        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun validateEditText(editText: EditText): Boolean {
        if (editText.text.isEmpty()) {
            editText.error = "Required"
            return false
        }
        return true
    }

    private fun showAlertFirstStart(){
        val fm: FragmentManager = supportFragmentManager
        val locationAlert = LocationAlertDialog()
        locationAlert.show(fm, "dialog_place_alert")
    }

    private fun hideProgressDialog() {
        progressDialog?.let { dialog ->
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
        progressDialog = null
    }

    private fun showProgressDialog() {
        if (progressDialog != null) {
            return
        }

        progressDialog = ProgressDialog(this).apply {
            setCancelable(false)
            setMessage("Loading...")
            show()
        }
    }

    private fun validateForm() = validateEditText(etEmail) && validateEditText(etPassword)

    private fun registerClick() {
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        firebaseAuth
            .createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
            .addOnSuccessListener { result ->
                hideProgressDialog()

                val firebaseUser = result.user
                val profileChangeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(firebaseUser?.email?.substringBefore('@'))
                    .build()
                firebaseUser?.updateProfile(profileChangeRequest)
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                hideProgressDialog()
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun loginClick() {
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        firebaseAuth
            .signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
            .addOnSuccessListener {
                hideProgressDialog()

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { exception ->
                hideProgressDialog()

                Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadSettings(){
        val sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        acceptedTerms = sharedPreferences.getBoolean(ACCEPT, false)
    }
}

