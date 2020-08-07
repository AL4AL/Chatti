package com.example.base.domain.model

import com.parse.ParseException
import com.parse.ParseUser

class User(
    username: String? = null,
    password: String? = null,
    phoneNumber: String? = null
) : ParseUser() {

    var phoneNumber: String?
        set(value) {
            put(PHONE_NUMBER, value!!)
        }
        get() = getString("phoneNumber")

    init {
        if (username != null &&
            password != null &&
            phoneNumber != null
        ) {
            this.username = username
            setPassword(password)
            this.phoneNumber = phoneNumber
        }
    }

    override fun signUp() {
        val query = getQuery()
        query.whereEqualTo(PHONE_NUMBER, phoneNumber)
        val users = query.find()
        if (users.size > 0) {
            throw ParseException(PHONE_NUMBER_ALREADY_TAKEN, "Phone number already taken")
        }
        super.signUp()
    }

    companion object {
        const val USERNAME = "username"
        const val PHONE_NUMBER = "phoneNumber"
        const val PHONE_NUMBER_ALREADY_TAKEN = 1001
    }
}