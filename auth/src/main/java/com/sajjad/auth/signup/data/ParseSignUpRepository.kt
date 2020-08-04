package com.sajjad.auth.signup.data

import com.example.base.domain.model.User
import com.sajjad.auth.signup.domain.repository.SignUpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ParseSignUpRepository @Inject constructor() : SignUpRepository {

    override suspend fun signUp(
        username: String,
        password: String,
        phoneNumber: String
    ) = withContext(Dispatchers.IO) {
        User(username, password, phoneNumber)
            .signUp()
    }
}