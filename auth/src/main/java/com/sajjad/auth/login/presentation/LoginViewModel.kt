package com.sajjad.auth.login.presentation

import androidx.lifecycle.viewModelScope
import com.parse.ParseException
import com.sajjad.auth.login.domain.usecase.LoginUseCase
import com.sajjad.base.domain.UseCaseHandler
import com.sajjad.base.presentation.viewmodel.BaseAction
import com.sajjad.base.presentation.viewmodel.BaseViewModel
import com.sajjad.base.presentation.viewmodel.BaseViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class LoginViewModel @Inject constructor(
    private val useCaseHandler: UseCaseHandler,
    private val loginUseCase: LoginUseCase
) : BaseViewModel<LoginViewModel.State, LoginViewModel.Action>(State()) {

    override fun onReduceState(viewAction: Action): State = state.copy(
        isSuccessful = viewAction is Action.Successful,
        invalidUserPass = viewAction is Action.Failure.InvalidUserPass,
        isError = viewAction is Action.Failure && viewAction !is Action.Failure.InvalidUserPass,
        isLoading = viewAction is Action.Loading
    )

    fun login(username: String, password: String) {
        val loginExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            sendAction(Action.Failure.UnknownError)
            Timber.e(throwable)
        }
        viewModelScope.launch(loginExceptionHandler) {
            sendAction(Action.Loading)
            val result = useCaseHandler.execute(
                loginUseCase,
                LoginUseCase.RequestValue(username, password)
            )
            when (result) {
                is LoginUseCase.Result.Success -> sendAction(Action.Successful)
                is LoginUseCase.Result.Error -> {
                    when (result.errorCode) {
                        ParseException.CONNECTION_FAILED -> sendAction(Action.Failure.ConnectionError)
                        ParseException.OBJECT_NOT_FOUND -> sendAction(Action.Failure.InvalidUserPass)
                        else -> sendAction(Action.Failure.UnknownError)
                    }
                }
            }
        }
    }

    internal sealed class Action : BaseAction {
        internal object Successful : Action()
        internal sealed class Failure : Action() {
            object ConnectionError : Failure()
            object InvalidUserPass : Failure()
            object UnknownError : Failure()
        }

        object Loading : Action()
    }

    internal data class State(
        val isSuccessful: Boolean = false,
        val invalidUserPass: Boolean = true,
        val isError: Boolean = false,
        val isLoading: Boolean = false
    ) : BaseViewState
}