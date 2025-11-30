package com.example.identikokiosk.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.identikokiosk.ui.details.BasicInfoActivity
import com.example.identikokiosk.ui.details.FamilyHealthActivity
import com.example.identikokiosk.ui.details.MedicalHistoryActivity
import com.example.identikokiosk.R
import com.example.identikokiosk.data.model.PatientData
import com.example.identikokiosk.ui.details.MedicalProfileActivity

// DELETED THIS LINE: private val DashboardActivity.detailsString: Any

class DashboardActivity : AppCompatActivity() {

    // Store the patient data to pass it to sub-screens
    private var patientData: PatientData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // 1. GET DATA (Handle both "Demo Mode" strings and "Real Object")
        // Check if we got the full object first (Preferred)
        patientData = intent.getSerializableExtra("PATIENT") as? PatientData

        // If null, try to reconstruct it from individual strings (Legacy/Demo compatibility)
        if (patientData == null) {
            val name = intent.getStringExtra("HOLDER_NAME") ?: "Unknown"
            val id = intent.getStringExtra("CARD_ID") ?: "---"
            val age = calculateAge(intent.getStringExtra("DOB") ?: "")
            // This local variable was likely intended, but it was using a null patientData object
            val detailsString = "$id • Unknown Sex • $age years"

            // Reconstruct a temporary object so the app doesn't crash on navigation
            patientData = PatientData(
                id, name, "N/A", "AA", "Male", "O+", 0.0,
                emptyList(), emptyList(), emptyList(), emptyList(),
                emptyList(), emptyList(), "Unknown", "Unknown", "Unknown"
            )
        }

        // 2. UPDATE HEADER UI
        findViewById<TextView>(R.id.tv_patient_name).text = patientData?.name
        findViewById<TextView>(R.id.tv_patient_details).text = "LAG-ID: ${patientData?.id}"
        findViewById<TextView>(R.id.tv_patient_id).text = patientData?.dateOfBirth

        findViewById<TextView>(R.id.btn_back).setOnClickListener { finish() }

        // 3. SETUP CARDS WITH NAVIGATION
        // We pass the Target Activity Class for each card
        setupCard(R.id.row_basic, "Basic Information", "Patient's essential details", R.color.card_blue, BasicInfoActivity::class.java)
        setupCard(R.id.row_medical, "Medical Profile", "Current health snapshot", R.color.card_blue, MedicalProfileActivity::class.java)
        setupCard(R.id.row_history, "Medical History", "Past conditions & treatments", R.color.card_blue, MedicalHistoryActivity::class.java)
        setupCard(R.id.row_family, "Family Health", "Genetic factors", R.color.card_blue, FamilyHealthActivity::class.java)
        //setupCard(R.id.card_emergency, "Emergency & Insurance", "Critical contact info", R.color.card_blue, EmergencyActivity::class.java)
    }

    private fun setupCard(includeId: Int, title: String, subtitle: String, colorResId: Int, targetActivity: Class<*>) {
        // FIX: You used 'include' in XML, which is a CardView root
        val row = findViewById<CardView>(includeId)

        // FIX: 'layout_card_bg' is inside the CardView
        val bgView = row.findViewById<LinearLayout>(R.id.layout_card_bg)
        bgView.setBackgroundColor(ContextCompat.getColor(this, colorResId))

        // Set Text
        row.findViewById<TextView>(R.id.tv_card_title).text = title
        row.findViewById<TextView>(R.id.tv_card_subtitle).text = subtitle

        // SETUP CLICK LISTENER
        row.setOnClickListener {
            val intent = Intent(this, targetActivity)
            // Pass the whole patient object to the next screen
            intent.putExtra("PATIENT", patientData)
            startActivity(intent)
        }
    }

    // Simple helper to parse "YYYY-MM-DD" and get age
    private fun calculateAge(dobString: String): String {
        if (dobString.isEmpty()) return "??"
        return try {
            // Assumes format "YYYY-MM-DD" like "1980-01-01"
            val birthYear = dobString.split("-")[0].toInt()
            val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
            (currentYear - birthYear).toString()
        } catch (e: Exception) {
            "N/A"
        }
    }
}
