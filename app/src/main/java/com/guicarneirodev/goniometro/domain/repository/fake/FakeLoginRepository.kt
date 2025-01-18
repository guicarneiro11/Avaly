package com.guicarneirodev.goniometro.domain.repository.fake

import com.guicarneirodev.goniometro.domain.repository.LoginRepository

class FakeLoginRepository : LoginRepository {
    override suspend fun signInWithEmail(email: String, password: String): Result<Unit> =
        Result.success(Unit)

    override suspend fun resetPassword(email: String): Result<Unit> =
        Result.success(Unit)

    override suspend fun sendSecurityCode(email: String): Result<Unit> =
        Result.success(Unit)

    override suspend fun verifySecurityCode(email: String, code: String): Result<Boolean> =
        Result.success(true)

    override suspend fun getIdToken(): Result<String> =
        Result.success("fake-token")
}