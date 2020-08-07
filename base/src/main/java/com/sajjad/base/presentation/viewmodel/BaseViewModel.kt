package com.sajjad.base.presentation.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sajjad.base.presentation.asLiveData
import kotlin.properties.Delegates

abstract class BaseViewModel<ViewState : BaseViewState, ViewAction : BaseAction>(initialState: ViewState) :
    ViewModel() {

    private val stateMutableLiveData = MutableLiveData<ViewState>()
    val stateLiveData = stateMutableLiveData.asLiveData()

    // Delegate can handle state event deduplication
    protected var state by Delegates.observable(initialState) { _, old, new ->
        stateMutableLiveData.value = new
    }

    init {
        state = initialState
    }

    @MainThread
    protected fun sendAction(viewAction: ViewAction) {
        state = onReduceState(viewAction)
    }

    protected abstract fun onReduceState(viewAction: ViewAction): ViewState
}
