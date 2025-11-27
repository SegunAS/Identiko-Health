package com.example.identikokiosk

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface HealthApi {

    @GET("{token}") // We pass "LAG1977019263" here
    suspend fun getPatient(@Path("token") token: String): PatientData

    companion object {
        // NOTE: Ensure your K5 is on the same network as this IP!
        private const val BASE_URL = "http://10.65.10.100:8080/"

        fun create(): HealthApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HealthApi::class.java)
        }
    }
}