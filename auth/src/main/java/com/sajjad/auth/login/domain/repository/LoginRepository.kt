package com.sajjad.auth.login.domain.repository

interface LoginRepository {
    suspend fun login(
        username: String,
        password: String
    )
}