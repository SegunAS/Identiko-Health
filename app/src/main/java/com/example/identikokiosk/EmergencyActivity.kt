// In EmergencyActivity.kt
val patient = intent.getSerializableExtra("PATIENT") as? PatientData ?: return

// Parse the raw strings (assuming your JSON sends them as single strings or you update Data Class to have nested objects)
// Example: "Sarah Anderson"
findViewById<TextView>(R.id.tv_contact_name).text = patient.emergencyContactName 
findViewById<TextView>(R.id.tv_contact_phone).text = patient.emergencyContactPhone

// Insurance
findViewById<TextView>(R.id.tv_insurance_provider).text = patient.insuranceProvider
// Use "Active" green dot logic
if (patient.insuranceStatus == "Active") {
    findViewById<TextView>(R.id.tv_status).setTextColor(Color.GREEN)
}