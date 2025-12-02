# Identiko Kiosk

> Self-service medical records kiosk application for Telpo K5 Android devices

[![Platform](https://img.shields.io/badge/platform-Android-green.svg)](https://developer.android.com/)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![Language](https://img.shields.io/badge/language-Kotlin-blue.svg)](https://kotlinlang.org/)
[![Version](https://img.shields.io/badge/version-1.0.0-orange.svg)](https://github.com)

## Overview

Identiko Kiosk is a self-service Android application designed for the **Telpo K5 hardware**. It allows patients to authenticate their identity using a **LAG-ID Smart Card (NFC)** and instantly view their medical records, insurance status, and emergency contacts.

The application is built for high-traffic environments (conventions, hospitals) and features an "Attract Loop," a secure loading state, and a read-only dashboard.

## Screenshots

![Scan Screen](./screenshots/scan.png) ![Dashboard](./screenshots/dashboard.png) ![Patient Info](./screenshots/patient-info.png)

## Features

- 🏥 **NFC Card Authentication** - Tap LAG-ID card to instantly access medical records
- 📊 **Comprehensive Patient Dashboard** - View all medical information at a glance
- 🔒 **Secure & Private** - Read-only access with automatic session timeout
- 📱 **Card-Based Navigation** - Easy access to different information categories
- 🎨 **Modern UI** - Clean, professional interface optimized for kiosk use
- 🌐 **Real-time Data** - Fetches latest patient information from secure API

## Table of Contents

- [Hardware Requirements](#hardware-requirements)
- [Installation](#installation)
- [Architecture](#architecture)
- [Key Features](#key-features)
- [User Flow](#user-flow)
- [API Integration](#api-integration)
- [Troubleshooting](#troubleshooting)
- [Development](#development)
- [Contributing](#contributing)
- [License](#license)

## Hardware Requirements

| Component | Requirement |
|-----------|-------------|
| **Device** | Telpo K5 Self-Service Kiosk (or compatible Android NFC Tablet) |
| **OS Version** | Android 7.0 (Nougat) or higher (API Level 24+) |
| **Connectivity** | Wi-Fi or Ethernet |
| **NFC** | Standard Android NFC SDK (ISO-DEP) |
| **Network** | Must be on same local network as API Server |

### NFC Setup

1. Navigate to **Settings → Connected Devices**
2. Enable **NFC** toggle
3. Ensure NFC is set to **reader mode**

## Installation

### Prerequisites

- Android Studio Arctic Fox or later
- JDK 8 or higher
- Telpo K5 device or Android emulator with NFC support

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/identiko-kiosk.git
   cd identiko-kiosk
   ```

2. **Configure API endpoint**
   
   Open `app/src/main/java/com/example/identikokiosk/data/api/HealthApi.kt`:
   ```kotlin
   private const val BASE_URL = "http://[YOUR_SERVER_IP]:8080/"
   ```

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Install on device**
   ```bash
   ./gradlew installDebug
   ```
   
   Or build APK manually:
   - **Build → Build Bundle(s) / APK(s) → Build APK(s)**
   - Copy `app-debug.apk` to USB drive
   - Install on Telpo K5 via File Manager

## Architecture

The app follows a modular architecture separating UI, Data, and Hardware logic:

```
com.example.identikokiosk
├── data
│   ├── api              # Network Layer (Retrofit)
│   │   └── HealthApi.kt
│   └── model            # Data Models
│       └── PatientData.kt
│
├── nfc                  # Hardware Logic
│   └── OptimizedCardDataReader.kt
│
└── ui
    ├── scan             # Attract & Loading Screens
    │   ├── ScanActivity.kt
    │   └── LoadingActivity.kt
    ├── dashboard        # Main Menu
    │   └── DashboardActivity.kt
    └── details          # Drill-down Information Screens
        ├── BasicInfoActivity.kt
        ├── MedicalProfileActivity.kt
        ├── MedicalHistoryActivity.kt
        ├── FamilyHealthActivity.kt
        └── EmergencyActivity.kt
```

## Key Features

### 1. NFC Reader (ISO-DEP)

Uses standard Android `IsoDep` technology to communicate with LAG-ID cards.

**File:** `OptimizedCardDataReader.kt`

**Logic:**
- Detects NFC tag
- Connects via IsoDep
- Sends APDU commands to select Application ID (AID)
- Extracts physical Card ID

**Flags:** Uses `NfcAdapter.FLAG_READER_NFC_A | FLAG_READER_NFC_B` for Mifare card compatibility

### 2. API & Networking

**Library:** Retrofit + Gson

**Endpoint:** `http://[LOCAL_IP]:8080/{card_id}`

**Security Note:** Uses HTTP (not HTTPS) with `android:usesCleartextTraffic="true"` enabled in Manifest

**Data Mapping:** Automatically converts snake_case JSON to camelCase Kotlin using `@SerializedName` annotations

### 3. Data Persistence

`PatientData` implements `Serializable` for seamless navigation:

**Flow:** Scan → API Call → PatientData Object → Dashboard → Detail Screens

**Crash Prevention:** Dashboard includes fallback "Demo User" if data is null (prevents crash on app restart)

## User Flow

```
1. Attract Screen (ScanActivity)
   ↓ [User taps NFC card]
   
2. Loading Screen (LoadingActivity)
   ↓ [API call & authentication]
   
3. Dashboard (DashboardActivity)
   ↓ [User selects category]
   
4. Detail Screens
   - Basic Information
   - Medical Profile
   - Medical History
   - Family Health
   - Emergency & Insurance
```

### Screen Descriptions

| Screen | Purpose | Key Features |
|--------|---------|--------------|
| **Attract Screen** | Idle state waiting for user | Blue gradient, pulsing NFC icon, tap-to-scan |
| **Loading Screen** | Handles async API call | Shows loading spinner, error handling |
| **Dashboard** | Central hub | Patient photo, name, age, 5 navigation cards |
| **Detail Screens** | Specific information views | Read-only data display, back navigation |

## API Integration

### Expected API Response

```json
{
  "card_id": "LAG-2024-8573",
  "full_name": "John Anderson",
  "date_of_birth": "1980-03-15",
  "blood_group": "O+",
  "genotype": "AA",
  "sex": "Male",
  "height": "178",
  "disabilities": "None",
  "underlying_conditions": ["Hypertension", "Type 2 Diabetes"],
  "allergies": ["Penicillin", "Peanuts"],
  "current_medications": [
    "Lisinopril 10mg - Once daily",
    "Metformin 500mg - Twice daily"
  ],
  "family_health_issues": [
    "Hypertension (Father)",
    "Type 2 Diabetes (Mother)"
  ],
  "previous_surgeries": [
    {
      "name": "Appendectomy",
      "date": "June 2015"
    }
  ],
  "organ_donor_status": "Registered Donor",
  "insurance_provider": "Blue Cross Blue Shield",
  "insurance_policy": "BCBS-457829-01",
  "insurance_group": "GRP-8847321",
  "insurance_status": "Active",
  "emergency_contact_name": "Sarah Anderson",
  "emergency_contact_relationship": "Spouse",
  "emergency_contact_phone": "+1 (555) 123-4567"
}
```

## Troubleshooting

| Issue | Cause | Solution |
|-------|-------|----------|
| **"Cleartext not permitted"** | Android blocks HTTP | Add `android:usesCleartextTraffic="true"` to Manifest |
| **"Socket failed: EACCES"** | Missing permission | Add `<uses-permission android:name="android.permission.INTERNET"/>` |
| **"Failed to connect to /10.65..."** | Wrong IP or Firewall | Ensure Kiosk & Server on same Wi-Fi. Check Windows Firewall |
| **"Layout width attribute missing"** | XML Syntax Error | Ensure every View has `layout_width` and `layout_height` |
| **App rotates sideways** | Kiosk auto-rotate | `android:screenOrientation="portrait"` in Manifest |
| **NFC not detecting** | NFC disabled | Enable in Settings → Connected Devices → NFC |

## Development

### Changing the Server IP

1. Open `data/api/HealthApi.kt`
2. Update:
   ```kotlin
   private const val BASE_URL = "http://[NEW_IP]:8080/"
   ```
3. Rebuild APK

### Adding a New Field

**Example: Adding "Blood Pressure"**

1. **Backend:** Ensure API sends the field
2. **Model:** Add to `PatientData.kt`:
   ```kotlin
   @SerializedName("blood_pressure")
   val bloodPressure: String
   ```
3. **UI:** Add to `BasicInfoActivity.kt`:
   ```kotlin
   addRow(container, "Blood Pressure", patient.bloodPressure)
   ```

### Updating App on Kiosk

1. Build APK: **Build → Build APK**
2. Copy `app-debug.apk` to USB drive
3. Insert USB into Telpo K5
4. Open File Manager → Install

## Tech Stack

- **Language:** Kotlin
- **UI Framework:** Android Views (XML)
- **Networking:** Retrofit 2 + Gson
- **NFC:** Android IsoDep API
- **Architecture:** MVC Pattern
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 33

## Dependencies

```gradle
dependencies {
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.google.code.gson:gson:2.10.1'
    implementation 'androidx.core:core-ktx:1.10.1'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.9.0'
}
```

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For issues, questions, or contributions, please open an issue in the GitHub repository.

## Acknowledgments

- Built for **Telpo K5** hardware
- Designed for healthcare environments
- Optimized for convention demonstrations

---

**Made with ❤️ for better healthcare accessibility**
