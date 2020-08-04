package com.sajjad.chat.conversation.domain.repository

interface AuthenticationRepository {
    suspend fun isAuthenticated(): Boolean
}