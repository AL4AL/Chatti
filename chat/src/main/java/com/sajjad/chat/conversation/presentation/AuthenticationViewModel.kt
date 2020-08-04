package com.sajjad.chat.conversation.presentation

import androidx.lifecycle.viewModelScope
import com.sajjad.base.domain.UseCaseHandler
import com.sajjad.base.presentation.viewmodel.BaseAction
import com.sajjad.base.presentation.viewmodel.BaseViewModel
import com.sajjad.base.presentation.viewmodel.BaseViewState
import com.sajjad.chat.conversation.domain.usecase.CheckAuthenticated
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class AuthenticationViewModel @Inject constructor(
    private val useCaseHandler: UseCaseHandler,
    private val checkAuthenticated: CheckAuthenticated
) : BaseViewModel<AuthenticationViewModel.State, AuthenticationViewModel.Action>(State()) {

    override fun onReduceState(viewAction: Action): State = state.copy(
        authenticated = if (viewAction is Action.Checking) {
            null
        } else {
            viewAction is Action.Authenticated
        },
        isLoading = viewAction is Action.Checking,
        isError = viewAction is Action.CheckFailed
    )

    fun authenticate() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            sendAction(Action.CheckFailed)
            Timber.e(throwable)
        }
        viewModelScope.launch(exceptionHandler) {
            sendAction(Action.Checking)
            val result = useCaseHandler.execute(
                checkAuthenticated,
                CheckAuthenticated.RequestValue
            )
            when (result) {
                is CheckAuthenticated.Result.Success -> sendAction(
                    if (result.authenticated) Action.Authenticated else Action.NotAuthenticated
                )
            }
        }
    }

    sealed class Action : BaseAction {
        object Checking : Action()
        object Authenticated : Action()
        object NotAuthenticated : Action()
        object CheckFailed : Action()
    }

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val authenticated: Boolean? = null
    ) : BaseViewState

}