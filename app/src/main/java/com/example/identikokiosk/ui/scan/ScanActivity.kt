package com.example.identikokiosk.ui.scan

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.identikokiosk.R
import com.example.identikokiosk.nfc.OptimizedCardDataReader
// Note: LoadingActivity import might need checking depending on your package structure
import com.example.identikokiosk.ui.scan.LoadingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScanActivity : AppCompatActivity(), NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter? = null
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        // 1. Setup Kiosk Mode (Hide Bars)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN)

        // 2. Setup UI Refs
        statusText = findViewById(R.id.tv_scan_title)

        // 3. Setup Button Logic (ONLY ONCE)
        // This launches the Loading Screen with a "Demo ID" so you can test the flow without a card
//        val scanButton = findViewById<Button>(R.id.btn_action_scan)
//        scanButton.setOnClickListener {
//            launchLoadingScreen("LAG1977019263") // Use Demo ID
//        }

        // 4. Initialize NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            statusText.text = "Error: No NFC Hardware"
        }
    }

    override fun onResume() {
        super.onResume()
        val options = Bundle()
        options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)

        // Enable Reader for Type A/B (Mifare/IsoDep)
        nfcAdapter?.enableReaderMode(
            this,
            this,
            NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
            options
        )
        statusText.text = "Ready to Scan"
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableReaderMode(this)
    }

    // This fires when a REAL card is tapped
    override fun onTagDiscovered(tag: Tag?) {
        val isoDep = IsoDep.get(tag) ?: return



        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val reader = OptimizedCardDataReader()
                val cardData = reader.readCardDataAsync(isoDep)

                // Use Real ID from card, or fallback if empty
                val scannedId = cardData.cardId

                if (scannedId.isNullOrEmpty()) {
                    statusText.text = "Error: Card ID not found"
                    return@launch
                }

                // Launch the Loading Activity
                launchLoadingScreen(scannedId)

            } catch (e: Exception) {
                // Log error silently
            }
        }
    }

    // Helper to switch screens cleanly
    private fun launchLoadingScreen(cardId: String) {
        val intent = Intent(this, LoadingActivity::class.java)
        intent.putExtra("CARD_ID", cardId)
        startActivity(intent)
        // We do NOT finish() here, so the user can come back later
        }
}