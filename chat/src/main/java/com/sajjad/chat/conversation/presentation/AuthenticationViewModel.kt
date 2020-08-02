package com.sajjad.chat.conversation.presentation

import androidx.lifecycle.viewModelScope
import com.parse.ParseUser
import com.sajjad.base.presentation.viewmodel.BaseAction
import com.sajjad.base.presentation.viewmodel.BaseViewModel
import com.sajjad.base.presentation.viewmodel.BaseViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class AuthenticationViewModel @Inject constructor() :
    BaseViewModel<AuthenticationViewModel.State, AuthenticationViewModel.Action>(State()) {

    override fun onReduceState(viewAction: Action): State = state.copy(
        authenticated = viewAction is Action.Authenticated,
        isLoading = viewAction is Action.Checking,
        isError = viewAction is Action.CheckFailed
    )

    fun authenticate() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            sendAction(Action.CheckFailed)
            Timber.e(throwable)
        }
        viewModelScope.launch {
            sendAction(Action.Checking)
            if (authenticated()) {
                sendAction(Action.Authenticated)
            } else {
                sendAction(Action.NotAuthenticated)
            }
        }
    }

    private fun authenticated(): Boolean = ParseUser.getCurrentUser() != null

    sealed class Action : BaseAction {
        object Checking : Action()
        object Authenticated : Action()
        object NotAuthenticated : Action()
        object CheckFailed : Action()
    }

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val authenticated: Boolean = false
    ) : BaseViewState

}