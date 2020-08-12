package com.sajjad.base.session

import androidx.lifecycle.MutableLiveData
import com.sajjad.base.presentation.asLiveData
import javax.inject.Inject
import kotlin.properties.Delegates

@SessionScope
class SessionHolder @Inject internal constructor() {
    private val sessionMutableLiveData = MutableLiveData<Session>()
    private var session: Session by Delegates.observable(
        Session()
    ) { _, _, newValue ->
        sessionMutableLiveData.postValue(newValue)
    }
    val sessionLiveData = sessionMutableLiveData.asLiveData()

    fun setAuthenticated(isAuthenticated: Boolean?) {
        this.session = session.copy(
            authenticated = isAuthenticated
        )
    }

    fun setUsername(username: String?) {
        this.session = session.copy(
            username = username
        )
    }

    fun setPhoneNumber(phoneNumber: String?) {
        this.session = session.copy(
            phoneNumber = phoneNumber
        )
    }
}

data class Session(
    val username: String? = null,
    val phoneNumber: String? = null,
    val authenticated: Boolean? = null
)