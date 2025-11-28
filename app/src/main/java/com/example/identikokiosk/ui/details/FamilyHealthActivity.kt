package com.example.identikokiosk.ui.details

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.identikokiosk.R
import com.example.identikokiosk.data.model.PatientData

class FamilyHealthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family_health)

        setupHeader("Family Health", "Understanding genetic factors", R.color.card_green)

        val patient = intent.getSerializableExtra("PATIENT") as? PatientData ?: return

        // 1. Family History List
        val container = findViewById<LinearLayout>(R.id.family_container)
        if (patient.familyHistory.isNotEmpty()) {
            patient.familyHistory.forEach { item ->
                // Add bullet point
                addBulletItem(container, item)
            }
        } else {
            addBulletItem(container, "No recorded family history")
        }

        // 2. Organ Donor Status
        val statusText = findViewById<TextView>(R.id.tv_donor_status)
        val status = patient.organDonorStatus // "Yes" or "No"

        if (status.equals("Yes", ignoreCase = true)) {
            statusText.text = "● Registered Donor"
            statusText.setTextColor(Color.parseColor("#10B981")) // Green
        } else {
            statusText.text = "● Not Registered"
            statusText.setTextColor(Color.GRAY)
        }
    }

    private fun addBulletItem(container: LinearLayout, text: String) {
        val textView = TextView(this).apply {
            this.text = "• $text" // Add bullet
            textSize = 20f
            setTextColor(Color.BLACK)
            setPadding(0, 8, 0, 8)
        }
        container.addView(textView)
    }

    private fun setupHeader(title: String, subtitle: String, colorRes: Int) {

        val headerContainer = findViewById<LinearLayout>(R.id.header_container)

        if (headerContainer == null) {
            println("Critical Error : Header Container is not found in familyhealthectivity")
            return
        }
        findViewById<LinearLayout>(R.id.header_container).setBackgroundResource(colorRes)
        findViewById<TextView>(R.id.tv_header_title).text = title
        findViewById<TextView>(R.id.tv_header_subtitle).text = subtitle
        findViewById<LinearLayout>(R.id.btn_back).setOnClickListener { finish() }
    }
}