package com.example.identikokiosk.ui.details

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.identikokiosk.R
import com.example.identikokiosk.data.model.PatientData

class MedicalHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_history)

        setupHeader("Medical History", "Past conditions & treatments", R.color.card_purple)

        val patient = intent.getSerializableExtra("PATIENT") as? PatientData ?: return
        val container = findViewById<LinearLayout>(R.id.surgeries_container)

        if (patient.surgeries.isNotEmpty()) {
            patient.surgeries.forEach { surgeryName ->
                // Since our current JSON only has the name (e.g., "Appendectomy"),
                // we display just that. If you update JSON to have dates, we can split string.
                addItem(container, surgeryName)
            }
        } else {
            addItem(container, "No recorded surgeries")
        }
    }

    private fun addItem(container: LinearLayout, title: String) {
        val titleView = TextView(this).apply {
            text = title
            textSize = 20f // Big font for Kiosk
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.BLACK)
            setPadding(0, 0, 0, 8)
        }

        // Add a Divider line
        val divider = View(this).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2)
            setBackgroundColor(Color.parseColor("#EEEEEE"))
            setPadding(0, 0, 0, 24)
        }

        // Wrapper for spacing
        val wrapper = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 16, 0, 16)
            addView(titleView)
            addView(divider)
        }

        container.addView(wrapper)
    }

    private fun setupHeader(title: String, subtitle: String, colorRes: Int) {
        findViewById<LinearLayout>(R.id.header_container).setBackgroundResource(colorRes)
        findViewById<TextView>(R.id.tv_header_title).text = title
        findViewById<TextView>(R.id.tv_header_subtitle).text = subtitle
        findViewById<LinearLayout>(R.id.btn_back).setOnClickListener { finish() }
    }
}