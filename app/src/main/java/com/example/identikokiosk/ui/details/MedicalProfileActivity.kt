package com.example.identikokiosk.ui.details

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.identikokiosk.R
import com.example.identikokiosk.data.model.PatientData
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class MedicalProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_profile)

        setupHeader("Medical Profile", "Current health snapshot", R.color.card_blue)

        val patient = intent.getSerializableExtra("PATIENT") as? PatientData ?: return

        // 1. Underlying Health Conditions (Blue Chips)
        val conditionGroup = findViewById<ChipGroup>(R.id.chip_group_conditions)

        if (patient.personalHistory.isNotEmpty()) {
            // Add real data (Light Blue bg, Dark Blue text)
            patient.personalHistory.forEach {
                addChip(conditionGroup, it, "#E3F2FD", "#1565C0")
            }
        } else {
            // Add "None" (Gray bg, Gray text)
            addChip(conditionGroup, "None", "#F5F5F5", "#757575")
        }

        // 2. Known Allergies (Red Chips)
        val allergyGroup = findViewById<ChipGroup>(R.id.chip_group_allergies)

        if (patient.allergies.isNotEmpty()) {
            // Add real data (Light Red bg, Dark Red text)
            patient.allergies.forEach {
                addChip(allergyGroup, it, "#FFEBEE", "#C62828")
            }
        } else {
            // Add "None" (Gray bg, Gray text)
            addChip(allergyGroup, "None", "#F5F5F5", "#757575")
        }
    }

    private fun addChip(group: ChipGroup, text: String, bgColor: String, textColor: String) {
        val chip = Chip(this).apply {
            this.text = text
            setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(bgColor)))
            setTextColor(Color.parseColor(textColor))
            textSize = 18f // Nice and big for Kiosk
            minHeight = 120 // Taller touch target
            ensureAccessibleTouchTarget(60)
            isClickable = false // Just for display
        }
        group.addView(chip)
    }

    // The Safe Header Function (Crash-Proof)
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
            android.util.Log.e("Identiko", "CRITICAL: Header not found in MedicalProfile")
            }
        }
}