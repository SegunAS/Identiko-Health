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
    private fun setupHeader(title: String, subtitle: String, colorRes: Int) {
        findViewById<LinearLayout>(R.id.header_container).setBackgroundResource(colorRes)
        findViewById<TextView>(R.id.tv_header_title).text = title
        findViewById<TextView>(R.id.tv_header_subtitle).text = subtitle
        findViewById<LinearLayout>(R.id.btn_back).setOnClickListener { finish() }
    }
}