package com.example.identikokiosk.ui.details

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.identikokiosk.R
import com.example.identikokiosk.data.model.PatientData

class EmergencyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency)

        setupHeader("Emergency & Insurance", "Critical contact info", R.color.card_orange)

        val patient = intent.getSerializableExtra("PATIENT") as? PatientData ?: return

        // 1. Set Emergency Contact
        // JSON only provides one string for contact, so we display it here
        findViewById<TextView>(R.id.tv_emergency_contact).text = patient.emergencyContact

        // 2. Set Insurance
        findViewById<TextView>(R.id.tv_insurance).text = patient.insurance
    }

   // PASTE THIS INTO ALL 5 DETAIL ACTIVITIES
   private fun setupHeader(title: String, subtitle: String, colorRes: Int) {
    // 1. Try finding by the "Include" ID (If you kept android:id="@+id/header" in XML)
    var headerView = findViewById<LinearLayout>(R.id.header)

    // 2. If null, try finding by the "Original" ID (If you removed the ID from XML)
    if (headerView == null) {
        headerView = findViewById(R.id.header_container)
    }

    // 3. If BOTH are null, the layout is broken. Log it, but DO NOT CRASH.
    if (headerView == null) {
        android.util.Log.e("Identiko", "CRITICAL: Header view not found. Check XML.")
        return // Exit function safely
    }

    // 4. Safe to set properties
    try {
        headerView.setBackgroundResource(colorRes)
        
        // Use safe calls (?.) just in case
        headerView.findViewById<TextView>(R.id.tv_header_title)?.text = title
        headerView.findViewById<TextView>(R.id.tv_header_subtitle)?.text = subtitle
        
        headerView.findViewById<LinearLayout>(R.id.btn_back)?.setOnClickListener { 
            finish() 
        }
    } catch (e: Exception) {
        android.util.Log.e("Identiko", "Error setting header data: ${e.message}")
    }
}
}