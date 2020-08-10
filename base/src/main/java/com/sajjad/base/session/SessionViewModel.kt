package com.sajjad.base.session

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sajjad.base.presentation.asLiveData

class SessionViewModel : ViewModel() {

    private val sessionMutableLiveData = MutableLiveData<Session>()
    val session = sessionMutableLiveData.asLiveData()


}