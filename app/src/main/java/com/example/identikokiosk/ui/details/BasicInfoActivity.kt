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

class BasicInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_info)

        // 1. Setup Header
        setupHeader("Basic Information", "Patient's essential details", R.color.brand_blue)

        // 2. Get Data
        val patient = intent.getSerializableExtra("PATIENT") as? PatientData ?: return

        // 3. Populate Rows (Only if data exists)
        val container = findViewById<LinearLayout>(R.id.info_container)

        addRow(container, "Name", patient.name)
        addRow(container, "Date of Birth", patient.dateOfBirth)
        addRow(container, "Blood Group", patient.bloodGroup, isRed = true) // Highlight red like image
        addRow(container, "Genotype", patient.genotype)
        addRow(container, "Sex", patient.sex)
        addRow(container, "Height", "${patient.height} cm")

        // Handle List -> String
        val disab = if(patient.disabilities.isEmpty()) "None" else patient.disabilities.joinToString(", ")
        addRow(container, "Disabilities", disab)
    }

    private fun addRow(container: LinearLayout, label: String, value: String?, isRed: Boolean = false) {
        if (value.isNullOrEmpty()) return // RULE: Do not add if empty

        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 24, 0, 24)
        }

        val labelView = TextView(this).apply {
            text = label
            textSize = 18f
            setTextColor(Color.parseColor("#666666"))
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val valueView = TextView(this).apply {
            text = value
            textSize = 18f
            setTextColor(if (isRed) Color.RED else Color.BLACK)
            textAlignment = TextView.TEXT_ALIGNMENT_VIEW_END
            setTypeface(null, Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        row.addView(labelView)
        row.addView(valueView)
        container.addView(row)

        // Add divider line
        val divider = View(this).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2)
            setBackgroundColor(Color.parseColor("#EEEEEE"))
        }
        container.addView(divider)
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