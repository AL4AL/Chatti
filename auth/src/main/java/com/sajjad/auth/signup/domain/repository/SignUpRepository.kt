package com.sajjad.auth.signup.domain.repository

interface SignUpRepository {
    suspend fun signUp(
        username: String,
        password: String,
        phoneNumber: String
    )
}