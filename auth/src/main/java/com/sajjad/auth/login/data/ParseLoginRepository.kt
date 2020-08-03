package com.sajjad.auth.login.data

import com.parse.ParseUser
import com.sajjad.auth.login.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ParseLoginRepository @Inject constructor() : LoginRepository {
    override suspend fun login(username: String, password: String) =
        withContext<Unit>(Dispatchers.IO) {
            ParseUser.logIn(username, password)
        }
}