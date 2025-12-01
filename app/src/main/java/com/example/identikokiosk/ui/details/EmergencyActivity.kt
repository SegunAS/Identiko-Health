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

        // 1. Setup Header (Using Orange to match the Dashboard card)
        setupHeader("Emergency Contact", "Critical contact info", R.color.card_blue)

        val patient = intent.getSerializableExtra("PATIENT") as? PatientData ?: return

        // 2. Set Name (NEW)
        // Uses the new field from your JSON update
        findViewById<TextView>(R.id.tv_emergency_name).text = patient.emergencyName ?: "N/A"

        // 3. Set Relationship (NEW)
        findViewById<TextView>(R.id.tv_emergency_relationship).text = patient.emergencyRelationship ?: "N/A"

        // 4. Set Phone Number (Existing)
        val contactView = findViewById<TextView>(R.id.tv_emergency_contact)
        if (patient.emergencyContact.isNotEmpty()) {
            contactView.text = patient.emergencyContact
        } else {
            contactView.text = "No contact provided"
        }
    }

    // The Safe Header Function
    private fun setupHeader(title: String, subtitle: String, colorRes: Int) {
        // Try finding by the original ID
        var headerView = findViewById<LinearLayout>(R.id.header_container)

        // Fallback: If null, try finding by the include ID (just in case)
        if (headerView == null) {
            headerView = findViewById(R.id.header_container)
        }

        if (headerView != null) {
            headerView.setBackgroundResource(colorRes)
            headerView.findViewById<TextView>(R.id.tv_header_title)?.text = title
            headerView.findViewById<TextView>(R.id.tv_header_subtitle)?.text = subtitle
            headerView.findViewById<LinearLayout>(R.id.btn_back)?.setOnClickListener {
                finish()
            }
        } else {
            android.util.Log.e("Identiko", "Header not found in EmergencyActivity")
            }
        }
}