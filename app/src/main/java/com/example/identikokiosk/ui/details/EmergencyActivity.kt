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

    private fun setupHeader(title: String, subtitle: String, colorRes: Int) {
        findViewById<LinearLayout>(R.id.header_container).setBackgroundResource(colorRes)
        findViewById<TextView>(R.id.tv_header_title).text = title
        findViewById<TextView>(R.id.tv_header_subtitle).text = subtitle
        findViewById<LinearLayout>(R.id.btn_back).setOnClickListener { finish() }
    }
}