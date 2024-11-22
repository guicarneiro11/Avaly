package com.guicarneirodev.goniometro.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

interface LoginRepository {
    suspend fun signInWithEmail(email: String, password: String): Result<Unit>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun sendSecurityCode(email: String): Result<Unit>
    suspend fun verifySecurityCode(email: String, code: String): Result<Boolean>
    suspend fun getIdToken(): Result<String>
}

class FirebaseRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFunctions: FirebaseFunctions
) : LoginRepository {
    override suspend fun signInWithEmail(email: String, password: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun resetPassword(email: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                firebaseAuth.sendPasswordResetEmail(email).await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun sendSecurityCode(email: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val sendEmailFunction = firebaseFunctions.getHttpsCallable("sendEmail")
                val result = sendEmailFunction.call(hashMapOf("email" to email)).await()
                val success = (result.data as? Map<*, *>)?.get("success") as? Boolean ?: false
                if (success) Result.success(Unit) else Result.failure(Exception("Failed to send security code"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun verifySecurityCode(email: String, code: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val verifyCodeFunction = firebaseFunctions.getHttpsCallable("verifySecurityCode")
                val result =
                    verifyCodeFunction.call(hashMapOf("email" to email, "code" to code)).await()
                val success = (result.data as? Map<*, *>)?.get("success") as? Boolean ?: false
                Result.success(success)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getIdToken(): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val token = firebaseAuth.currentUser?.getIdToken(false)?.await()?.token
                if (token != null) {
                    Result.success(token)
                } else {
                    Result.failure(Exception("Failed to get token"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}