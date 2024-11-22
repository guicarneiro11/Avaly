package com.guicarneirodev.goniometro.data.service

import android.util.Log
import com.guicarneirodev.goniometro.data.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import kotlinx.serialization.Serializable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.http.Header

@Serializable
data class EmailRequestDTO(
    val email: String
)

interface SendPdfApi {
    @POST("api/patients/{patientId}/report")
    suspend fun sendPdfToEmail(
        @Path("patientId") patientId: String,
        @Header("Authorization") authToken: String,
        @Body emailRequest: EmailRequestDTO
    ): Response<Map<String, String>>
}

object RetrofitInstance {
    private const val BASE_URL = "http://18.231.217.128:8080/"

    val retrofit: Retrofit by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor())
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

interface PdfService {
    suspend fun sendPdfToEmail(userId: String, patientId: String, email: String): Result<Unit>
}

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        Log.d("HTTP_CLIENT", "Request: ${request.method} ${request.url}")

        val response = chain.proceed(request)
        Log.d("HTTP_CLIENT", "Response Code: ${response.code}")

        return response
    }
}

class RetrofitPdfService(private val loginRepository: LoginRepository) : PdfService {
    private val api: SendPdfApi = RetrofitInstance.retrofit.create(SendPdfApi::class.java)

    override suspend fun sendPdfToEmail(userId: String, patientId: String, email: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val token = loginRepository.getIdToken().getOrThrow()
                val response = api.sendPdfToEmail(
                    patientId = patientId,
                    authToken = "Bearer $token",
                    emailRequest = EmailRequestDTO(email)
                )

                if (response.isSuccessful) Result.success(Unit)
                else Result.failure(Exception("Failed: ${response.code()}"))

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}