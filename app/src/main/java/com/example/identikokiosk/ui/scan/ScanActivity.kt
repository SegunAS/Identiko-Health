package com.example.identikokiosk.ui.scan

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.identikokiosk.MainActivity
import com.example.identikokiosk.R
import com.example.identikokiosk.data.api.HealthApi
import com.example.identikokiosk.data.model.PatientData
import com.example.identikokiosk.nfc.OptimizedCardDataReader
import com.example.identikokiosk.ui.dashboard.DashboardActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScanActivity : AppCompatActivity(), NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter? = null
    private lateinit var statusText: TextView
    private lateinit var statusPill: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        val scanButton: Button = findViewById(R.id.btn_action_scan)

        scanButton.setOnClickListener{
            val intent = Intent(this, com.example.identikokiosk.ui.dashboard.DashboardActivity::class.java)

            intent.putExtra("HOLDER_NAME", "Demo User")
            intent.putExtra("CARD_ID", "MANUAL-BYPASS-01")

            startActivity(intent)
        }



        // Kiosk Mode: Hide System Bars
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN)

        // UI References
        statusText = findViewById(R.id.tv_scan_title)
       // statusPill = findViewById(R.id.tv_app_name) // Using app name as status area, or find the "Ready" pill

        // 1. Initialize NFC Adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcAdapter == null) {
            statusText.text = "Error: No NFC Hardware"
            return
        }

        // Manual Button (Fallback or Demo purposes)
        findViewById<Button>(R.id.btn_action_scan).setOnClickListener {
            Toast.makeText(this, "Please tap card on the reader", Toast.LENGTH_SHORT).show()
        }
    }

    // 2. Start Listening when App is open
    override fun onResume() {
        super.onResume()
        if (nfcAdapter != null) {
            val options = Bundle()
            // Low debounce to allow quick reads
            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)

            val flags = NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK


            // Listen for ISO-DEP (Smart Cards) type tags
            nfcAdapter!!.enableReaderMode(
                this,
                this,
                flags,
                options
            )
            updateStatus("Ready to Scan")
        }
    }

    // 3. Stop Listening when App is paused
    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableReaderMode(this)
    }

    // 4. THIS IS THE MAGIC: When a card is tapped
    override fun onTagDiscovered(tag: Tag?) {
        if (tag == null) return

        // 1. SHOW SPINNER
        runOnUiThread {
            findViewById<View>(R.id.loading_overlay).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_loading_status).text = "Reading Secure Sector..."
        }

        // Check for correct card type
        val isoDep = IsoDep.get(tag)
        if (isoDep == null) {
            runOnUiThread {
                findViewById<View>(R.id.loading_overlay).visibility = View.GONE
                updateStatus("Error: Card type not supported")
            }
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // 2. READ THE CARD ID
                val reader = OptimizedCardDataReader()
                val cardData = reader.readCardDataAsync(isoDep)

                // Fallback: If card is empty/error, use the Demo ID "LAG1977019263"
                // (Change this logic if you want to fail strictly on bad cards)
                val scannedId = cardData.cardId ?: "LAG123456789"

                // 3. UPDATE UI STATUS
                withContext(Dispatchers.Main) {
                    findViewById<TextView>(R.id.tv_loading_status).text =
                        "Fetching Health Records..."
                }

                // 4. CALL THE API (The Missing Link)
                val api = HealthApi.Companion.create()
                val patientProfile = api.getPatient(scannedId)

                // 5. SUCCESS! GO TO DASHBOARD
                withContext(Dispatchers.Main) {
                    Log.d("NFC", "API Success: ${patientProfile.name}")
                    navigateToDashboard(patientProfile) // Pass the API data, NOT the card data
                }

            } catch (e: Exception) {
                // 6. ERROR HANDLING
                withContext(Dispatchers.Main) {
                    // Hide spinner
                    findViewById<View>(R.id.loading_overlay).visibility = View.GONE

                    // Show error message
                    val errorMsg = "Error: ${e.message}"
                    updateStatus(errorMsg)
                    Toast.makeText(this@ScanActivity, errorMsg, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun navigateToDashboard(patient: PatientData) {
    val intent = Intent(this, DashboardActivity::class.java)

    // CRITICAL: Pass the entire object using Serializable
    intent.putExtra("PATIENT", patient)

    // Pass these too just in case you need them for simple display
    intent.putExtra("HOLDER_NAME", patient.name)
    intent.putExtra("CARD_ID", patient.id)

    startActivity(intent)
    findViewById<View>(R.id.loading_overlay).visibility = View.GONE
}

    private fun updateStatus(msg: String) {
        // Find the "Ready to Scan" text and update it
        statusText.text = msg
    }
}