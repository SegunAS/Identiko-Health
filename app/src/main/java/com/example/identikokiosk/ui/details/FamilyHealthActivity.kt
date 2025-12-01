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

        setupHeader("Family Health", "Understanding genetic factors", R.color.card_blue)

        val patient = intent.getSerializableExtra("PATIENT") as? PatientData ?: return

        // 1. POPULATE FAMILY HISTORY
        val container = findViewById<LinearLayout>(R.id.family_container)

        if (patient.familyHistory.isNotEmpty()) {
            patient.familyHistory.forEach { item ->
                addBulletItem(container, item)
            }
        } else {
            addBulletItem(container, "No recorded family history")
        }
    }

    private fun addBulletItem(container: LinearLayout, text: String) {
        val textView = TextView(this).apply {
            this.text = "• $text" // Add bullet point
            textSize = 20f
            setTextColor(Color.BLACK)
            setPadding(0, 8, 0, 8)
        }
        container.addView(textView)
    }

    // The Safe Header Function
    private fun setupHeader(title: String, subtitle: String, colorRes: Int) {
        var headerView = findViewById<LinearLayout>(R.id.header_container)

        // Fallback for ID mismatch
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
            android.util.Log.e("Identiko", "Header not found in FamilyHealthActivity")
            }
        }
}