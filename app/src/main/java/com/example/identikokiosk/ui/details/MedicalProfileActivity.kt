package com.example.identikokiosk.ui.details


import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity // <-- Import this
import com.example.identikokiosk.R
import com.example.identikokiosk.data.model.PatientData
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
class MedicalProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_profile)

        setupHeader("Medical Profile", "Current health snapshot", R.color.card_red) // Red/Pink
        val patient = intent.getSerializableExtra("PATIENT") as? PatientData ?: return

        // 1. Underlying Conditions (Blue Pills)
        val conditionGroup = findViewById<ChipGroup>(R.id.chip_group_conditions)
        if (patient.personalHistory.isNotEmpty()) {
            patient.personalHistory.forEach { addChip(conditionGroup, it, "#E3F2FD", "#1565C0") } // Light Blue bg, Dark Blue text
        } else {
             findViewById<View>(R.id.card_conditions).visibility = View.GONE
        }

        // 2. Allergies (Red/Pink Pills)
        val allergyGroup = findViewById<ChipGroup>(R.id.chip_group_allergies)
        if (patient.allergies.isNotEmpty()) {
            patient.allergies.forEach { addChip(allergyGroup, it, "#FFEBEE", "#C62828") } // Light Red bg, Dark Red text
        } else {
             findViewById<View>(R.id.card_allergies).visibility = View.GONE
        }
    }

    private fun addChip(group: ChipGroup, text: String, bgColor: String, textColor: String) {
        val chip = Chip(this).apply {
            this.text = text
            setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(bgColor)))
            setTextColor(Color.parseColor(textColor))
            textSize = 16f
            ensureAccessibleTouchTarget(50) // Make it nice and big for Kiosk
        }
        group.addView(chip)
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