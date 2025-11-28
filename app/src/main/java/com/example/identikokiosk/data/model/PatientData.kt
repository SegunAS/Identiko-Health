package com.example.identikokiosk.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PatientData(
    val id: String,
    val name: String,

    @SerializedName("dob")
    val dateOfBirth: String,

    val genotype: String,
    val sex: String,

    @SerializedName("blood_group")
    val bloodGroup: String,

    val height: Double,
    val disabilities: List<String>,

    @SerializedName("personal_history")
    val personalHistory: List<String>,

    @SerializedName("family_history")
    val familyHistory: List<String>,

    val medications: List<String>,
    val surgeries: List<String>,
    val allergies: List<String>,

    @SerializedName("organ_donor_status")
    val organDonorStatus: String,

    val insurance: String,

    @SerializedName("emergency_contact")
    val emergencyContact: String
) : Serializable