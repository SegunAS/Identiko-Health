package com.example.identikokiosk.ui.details

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.identikokiosk.R
import com.example.identikokiosk.data.model.PatientData

class MedicalHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_history)

        setupHeader("Medical History", "Past conditions & treatments", R.color.card_blue)

        val patient = intent.getSerializableExtra("PATIENT") as? PatientData ?: return

        // 1. POPULATE MEDICATIONS (New)
        val medsContainer = findViewById<LinearLayout>(R.id.medications_container)

        if (patient.medications.isNotEmpty()) {
            patient.medications.forEach { med ->
                addItem(medsContainer, med)
            }
        } else {
            addItem(medsContainer, "No active medications")
        }

        // 2. POPULATE SURGERIES (Existing)
        val surgeryContainer = findViewById<LinearLayout>(R.id.surgeries_container)

        if (patient.surgeries.isNotEmpty()) {
            patient.surgeries.forEach { surgeryName ->
                addItem(surgeryContainer, surgeryName)
            }
        } else {
            addItem(surgeryContainer, "No recorded surgeries")
        }
    }

    // Helper to add rows nicely
    private fun addItem(container: LinearLayout, title: String) {
        val titleView = TextView(this).apply {
            text = title
            textSize = 20f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setTextColor(Color.BLACK)
            setPadding(0, 0, 0, 8)
        }

        val divider = android.view.View(this).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2)
            setBackgroundColor(Color.parseColor("#EEEEEE"))
        }

        val wrapper = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 16, 0, 16)
            addView(titleView)
            addView(divider)
        }

        container.addView(wrapper)
    }

    // The Safe Header Function
    private fun setupHeader(title: String, subtitle: String, colorRes: Int) {
        var headerView = findViewById<LinearLayout>(R.id.header_container)

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
            android.util.Log.e("Identiko", "Header not found in MedicalHistory")
            }
        }
}