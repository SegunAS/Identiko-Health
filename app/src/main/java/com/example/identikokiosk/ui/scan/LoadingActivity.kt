package com.example.identikokiosk.ui.scan // Place in 'scan' or 'ui' package

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.identikokiosk.R
import com.example.identikokiosk.data.api.HealthApi
import com.example.identikokiosk.data.model.PatientData
import com.example.identikokiosk.ui.dashboard.DashboardActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        // 1. Get the Scanned ID from the Intent
        val cardId = intent.getStringExtra("CARD_ID") ?: "LAG1977019263"

        // 2. Start the API Call
        fetchPatientData(cardId)
    }

    private fun fetchPatientData(cardId: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Optional: Fake delay so user sees the "Loading" animation (UX)
                delay(1500) 

                // A. Call API
                val api = HealthApi.create()
                val patient = api.getPatient(cardId)

                // B. Success -> Go to Dashboard
                withContext(Dispatchers.Main) {
                    val intent = Intent(this@LoadingActivity, DashboardActivity::class.java)
                    intent.putExtra("PATIENT", patient)
                    startActivity(intent)
                    finish() // Close loading screen so Back button goes to Scanner, not Loading
                }
            } catch (e: Exception) {
                // C. Error -> Go back to Scanner
                withContext(Dispatchers.Main) {
                    val statusText = findViewById<TextView>(R.id.tv_status)
                    statusText.text = "Error: ${e.message}"
                    statusText.setTextColor(android.graphics.Color.RED)
                    
                    // Wait 2 seconds so user sees error, then close
                    delay(2000)
                    finish()
                }
            }
        }
    }
}