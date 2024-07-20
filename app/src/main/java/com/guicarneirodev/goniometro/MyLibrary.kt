package com.guicarneirodev.goniometro

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64

interface BioDigitalApi {
    @GET("services/v2/content/collections/myhuman")
    suspend fun getMyHumanModels(
        @Header("Authorization") authHeader: String,
        @Header("Accept") acceptHeader: String
    ): MyHumanResponse
}

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://apis.biodigital.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val bioDigitalApi: BioDigitalApi = retrofit.create(BioDigitalApi::class.java)

data class MyHumanResponse(
    val service_version: Int,
    val myhuman: List<Model>
)

data class Model(
    val content_id: String,
    val content_title: String,
    val content_type: String,
    val content_url: String,
    val content_thumbnail_url: String
)

data class RequestObject(val grant_type: String, val scope: String)
data class ResponseObject(val access_token: String, val token_type: String, val expires_in: Int)

object TokenManager {
    var developerKey: String? = null
    var developerSecret: String? = null
    private var accessToken: String? = null
    private var tokenExpiryTime: Long = 0

    suspend fun getAccessToken(): String {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val developerKey = remoteConfig.getString("DEVELOPER_KEY")
        val developerSecret = remoteConfig.getString("DEVELOPER_SECRET")

        val currentTime = System.currentTimeMillis()
        return withContext(Dispatchers.IO) {
            if (accessToken == null || currentTime >= tokenExpiryTime) {
                val newToken = fetchAccessToken(developerKey, developerSecret)
                accessToken = newToken.first
                tokenExpiryTime = newToken.second
            }
            accessToken!!
        }
    }

    private suspend fun fetchAccessToken(developerKey: String, developerSecret: String): Pair<String, Long> {
        return withContext(Dispatchers.IO) {
            val url = "https://apis.biodigital.com/oauth2/v2/token"
            val authStr = Base64.getEncoder().encodeToString("$developerKey:$developerSecret".toByteArray(Charsets.UTF_8))

            val postDataObj = RequestObject("client_credentials", "contentapi")
            val gson = GsonBuilder().create()
            val postDataJson = gson.toJson(postDataObj)

            val connection = buildPostRequest(url, postDataJson, authStr)
            val response = buildResponse(connection)

            val responseObj = gson.fromJson(response, ResponseObject::class.java)
            val expiryTime = System.currentTimeMillis() + responseObj.expires_in * 1000
            Pair(responseObj.access_token, expiryTime)
        }
    }
}

fun buildPostRequest(url: String, content: String, authStr: String): HttpURLConnection {
    val requestURL = URL(url)
    val connection = requestURL.openConnection() as HttpURLConnection
    connection.doInput = true
    connection.doOutput = true
    connection.requestMethod = "POST"
    connection.setRequestProperty("Authorization", "Basic $authStr")
    connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
    connection.setRequestProperty("Accept", "application/json")
    connection.setRequestProperty("Content-Length", "${content.toByteArray().size}")
    connection.useCaches = false
    OutputStreamWriter(connection.outputStream).use {
        it.write(content)
        it.flush()
    }
    return connection
}

fun buildResponse(connection: HttpURLConnection): String {
    val response = StringBuilder()
    BufferedReader(InputStreamReader(connection.inputStream)).use { it ->
        var inputLine: String?
        while (it.readLine().also { inputLine = it } != null) {
            response.append(inputLine)
        }
    }
    return response.toString()
}

suspend fun fetchMyHumanModels(accessToken: String): List<Model> {
    val authHeader = "Bearer $accessToken"
    val acceptHeader = "application/json"

    return try {
        val response = bioDigitalApi.getMyHumanModels(authHeader, acceptHeader)
        response.myhuman
    } catch (e: Exception) {
        println("Request failed: ${e.message}")
        emptyList()
    }
}