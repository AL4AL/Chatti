package com.sajjad.auth.signup.presentation

import androidx.lifecycle.viewModelScope
import com.example.base.domain.model.User
import com.example.base.formatPhoneNumber
import com.parse.ParseException
import com.sajjad.auth.signup.domain.usecase.SignUpUseCase
import com.sajjad.base.domain.UseCaseHandler
import com.sajjad.base.presentation.viewmodel.BaseAction
import com.sajjad.base.presentation.viewmodel.BaseViewModel
import com.sajjad.base.presentation.viewmodel.BaseViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class SignUpViewModel @Inject constructor(
    private val useCaseHandler: UseCaseHandler,
    private val signUpUseCase: SignUpUseCase
) : BaseViewModel<SignUpViewModel.ViewState, SignUpViewModel.Action>(
    ViewState()
) {

    override fun onReduceState(viewAction: Action): ViewState = state.copy(
        isLoading = viewAction is Action.Signing,
        isError = viewAction is Action.Error,
        isSuccessful = viewAction is Action.Signed,
        isUsernameTaken = viewAction is Action.UsernameTaken,
        phoneNumberTaken = viewAction is Action.PhoneNumberTaken
    )

    fun signUp(
        username: String,
        password: String,
        phoneNumber: String
    ) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            sendAction(Action.Error)
            Timber.e(throwable)
        }
        viewModelScope.launch(exceptionHandler) {
            sendAction(Action.Signing)
            val result = useCaseHandler.execute(
                signUpUseCase,
                SignUpUseCase.RequestValues(
                    username,
                    password,
                    phoneNumber.formatPhoneNumber()
                )
            )
            when (result) {
                is SignUpUseCase.Result.Success -> sendAction(Action.Signed)
                is SignUpUseCase.Result.Error -> when (result.errorCode) {
                    ParseException.USERNAME_TAKEN -> sendAction(Action.UsernameTaken)
                    User.PHONE_NUMBER_ALREADY_TAKEN -> sendAction(Action.PhoneNumberTaken)
                    else -> sendAction(Action.Error)
                }
            }
        }
    }

    internal data class ViewState(
        val isLoading: Boolean = false,
        val isUsernameTaken: Boolean = false,
        val phoneNumberTaken: Boolean = false,
        val isError: Boolean = false,
        val isSuccessful: Boolean = false
    ) : BaseViewState

    internal sealed class Action : BaseAction {
        object Signing : Action()
        object UsernameTaken : Action()
        object PhoneNumberTaken : Action()
        object Signed : Action()
        object Error : Action()
    }
}