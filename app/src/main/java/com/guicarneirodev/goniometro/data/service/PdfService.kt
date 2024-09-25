package com.guicarneirodev.goniometro.data.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SendPdfApi {
    @GET("api/users/{userId}/patients/{patientId}/send-pdf")
    suspend fun sendPdfToEmail(
        @Path("userId") userId: String,
        @Path("patientId") patientId: String,
        @Query("email") email: String
    ): Response<Unit>
}

object RetrofitInstance {
    private const val BASE_URL = "https://ktor-app-cc5gi2t6tq-rj.a.run.app"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

interface PdfService {
    suspend fun sendPdfToEmail(userId: String, patientId: String, email: String): Result<Unit>
}

class RetrofitPdfService : PdfService {
    private val api: SendPdfApi = RetrofitInstance.retrofit.create(SendPdfApi::class.java)

    override suspend fun sendPdfToEmail(userId: String, patientId: String, email: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.sendPdfToEmail(userId, patientId, email)
                if (response.isSuccessful) Result.success(Unit) else Result.failure(Exception("Failed to send PDF"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
