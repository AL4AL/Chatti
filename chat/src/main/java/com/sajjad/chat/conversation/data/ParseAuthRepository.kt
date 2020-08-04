package com.sajjad.chat.conversation.data

import com.parse.ParseUser
import com.sajjad.chat.conversation.domain.repository.AuthenticationRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ParseAuthRepository @Inject constructor() : AuthenticationRepository {
    override suspend fun isAuthenticated(): Boolean =
        withContext(IO) { ParseUser.getCurrentUser() != null }
}