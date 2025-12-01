package com.example.identikokiosk.data.api

import com.example.identikokiosk.data.model.PatientData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
interface HealthApi {

    @GET("{token}") // We pass "LAG1977019263" here
    suspend fun getPatient(@Path("token") token: String): PatientData

    companion object {
        // NOTE: Ensure your K5 is on the same network as this IP!
        private const val BASE_URL = "https://test-demo-scanner.onrender.com/"
        //https://test-demo-scanner.onrender.com
        fun create(): HealthApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HealthApi::class.java)
        }
    }
}