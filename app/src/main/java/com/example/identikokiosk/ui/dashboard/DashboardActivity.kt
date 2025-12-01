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
import com.example.identikokiosk.ui.details.EmergencyActivity
import com.example.identikokiosk.ui.details.MedicalProfileActivity

class DashboardActivity : AppCompatActivity() {

    private var patientData: PatientData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // 1. GET DATA
        patientData = intent.getSerializableExtra("PATIENT") as? PatientData

        // 2. CHECK FOR NULL (Demo Mode / Error Prevention)
        if (patientData == null) {
            // FIX: We must actually create the object if it's missing
            patientData = PatientData(
                id = "DEMO-000",
                name = "Demo User",
                dateOfBirth = "1990-01-01",
                genotype = "AA",
                sex = "Male",
                bloodGroup = "O+",
                height = 180.0,
                disabilities = listOf("None"),
                personalHistory = emptyList(),
                familyHistory = emptyList(),
                medications = emptyList(),
                surgeries = emptyList(),
                allergies = emptyList(),
                organDonorStatus = "No",
                insurance = "None",
                insuranceId = "None",
                emergencyName = "",
                emergencyRelationship = "",
                emergencyContact = "N/A"
            )
        }

        // 3. DETERMINE WHICH ID TO SHOW
        // Priority: Physical Scanned ID > Database ID > "Unknown"
        val scannedId = intent.getStringExtra("SCANNED_CARD_ID")
        val displayId = scannedId ?: patientData?.id ?: "Unknown"

        // 4. CALCULATE AGE
        val age = calculateAge(patientData?.dateOfBirth ?: "")

        // 5. UPDATE HEADER UI
        findViewById<TextView>(R.id.tv_patient_name).text = patientData?.name

        // FIX: Combine ID, Sex, and Age into one string
        val detailString = "$displayId"
        findViewById<TextView>(R.id.tv_patient_id).text = detailString

        // Back Button
        findViewById<TextView>(R.id.btn_back).setOnClickListener { finish() }

        // 6. SETUP CARDS
        setupCard(R.id.row_basic, "Basic Information", "Patient's essential details", R.color.card_blue, BasicInfoActivity::class.java)
        setupCard(R.id.row_medical, "Medical Profile", "Current health snapshot", R.color.card_blue, MedicalProfileActivity::class.java)
        setupCard(R.id.row_history, "Medical History", "Past conditions & treatments", R.color.card_blue, MedicalHistoryActivity::class.java)
        setupCard(R.id.row_family, "Family Health", "Genetic factors", R.color.card_blue, FamilyHealthActivity::class.java)
        setupCard(R.id.row_emergency, "Emergency Contact", "Critical contact info", R.color.card_blue, EmergencyActivity::class.java)
    }

    private fun setupCard(includeId: Int, title: String, subtitle: String, colorResId: Int, targetActivity: Class<*>) {
        val row = findViewById<CardView>(includeId)
        val bgView = row.findViewById<LinearLayout>(R.id.layout_card_bg)

        bgView.setBackgroundColor(ContextCompat.getColor(this, colorResId))

        row.findViewById<TextView>(R.id.tv_card_title).text = title
        row.findViewById<TextView>(R.id.tv_card_subtitle).text = subtitle

        row.setOnClickListener {
            val intent = Intent(this, targetActivity)
            intent.putExtra("PATIENT", patientData)
            startActivity(intent)
        }
    }

    private fun calculateAge(dobString: String): String {
        if (dobString.isEmpty()) return "??"
        return try {
            val birthYear = dobString.split("-")[0].toInt()
            val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
            (currentYear - birthYear).toString()
        } catch (e: Exception) {
            "N/A"
            }
        }}
