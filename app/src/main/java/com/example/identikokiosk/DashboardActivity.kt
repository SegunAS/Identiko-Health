package com.example.identikokiosk

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // 1. Get Data from ScanActivity
        val holderName = intent.getStringExtra("HOLDER_NAME") ?: "Unknown User"
        val cardId = intent.getStringExtra("CARD_ID") ?: "----"

        // 2. Find the Header TextViews
        val nameText = findViewById<TextView>(R.id.tv_patient_name)
        val detailsText = findViewById<TextView>(R.id.tv_patient_details)

        // 3. Update UI
        nameText.text = holderName
        detailsText.text = "LAG-ID: $cardId"


        // Setup the Back Button
        findViewById<TextView>(R.id.btn_back).setOnClickListener { finish() }

        // Setup the colorful cards
        setupCard(R.id.card_basic, "Basic Information", "Patient's essential details", R.color.card_blue)
        setupCard(R.id.card_medical, "Medical Profile", "Current health snapshot", R.color.card_red)
        setupCard(R.id.card_history, "Medical History", "Past conditions & treatments", R.color.card_purple)
        setupCard(R.id.card_family, "Family Health", "Genetic factors", R.color.card_green)
        setupCard(R.id.card_emergency, "Emergency & Insurance", "Critical contact info", R.color.card_orange)
    }

    private fun setupCard(includeId: Int, title: String, subtitle: String, colorResId: Int) {
        val card = findViewById<LinearLayout>(includeId)
        val bgLayout = card.findViewById<LinearLayout>(R.id.card_background)

        bgLayout.setBackgroundColor(ContextCompat.getColor(this, colorResId))

        card.findViewById<TextView>(R.id.tv_card_title).text = title
        card.findViewById<TextView>(R.id.tv_card_subtitle).text = subtitle
    }
}