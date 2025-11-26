package com.example.identikokiosk

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

            // Listen for ISO-DEP (Smart Cards) type tags
            nfcAdapter!!.enableReaderMode(
                this,
                this,
                NfcAdapter.FLAG_READER_NFC_A or
                        NfcAdapter.FLAG_READER_NFC_B or
                        NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
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

        // Update UI to show we are working
        runOnUiThread { updateStatus("Reading Card...") }

        // Is it an IsoDep card? (Compatible with your Reader Code)
        val isoDep = IsoDep.get(tag)
        if (isoDep == null) {
            runOnUiThread { updateStatus("Error: Card type not supported") }
            return
        }

        // Launch Coroutine to read data (Background Thread)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Initialize your provided Reader Class
                val reader = OptimizedCardDataReader()

                // CALL YOUR PROVIDED FUNCTION
                val cardData = reader.readCardDataAsync(isoDep)

                withContext(Dispatchers.Main) {
                    if (cardData.cardId != null || cardData.holderName != null) {
                        // SUCCESS!
                        Log.d("NFC", "Success: ${cardData.holderName}")
                        navigateToDashboard(cardData)
                    } else {
                        // FAILED TO READ DATA
                        updateStatus("Scan Failed. Try Again.")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    updateStatus("Error: ${e.message}")
                }
            }
        }
    }

    private fun navigateToDashboard(data: OptimizedCardDataReader.CardData) {
        val intent = Intent(this, DashboardActivity::class.java)

        // Pass the data to the next screen
        intent.putExtra("CARD_ID", data.cardId)
        intent.putExtra("HOLDER_NAME", data.holderName)
        // You can pass other fields here

        startActivity(intent)
        // Don't finish() if you want the user to be able to go back easily
    }

    private fun updateStatus(msg: String) {
        // Find the "Ready to Scan" text and update it
        statusText.text = msg
    }
}