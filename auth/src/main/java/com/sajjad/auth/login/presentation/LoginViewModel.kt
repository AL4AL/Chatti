package com.sajjad.auth.login.presentation

import com.sajjad.base.presentation.viewmodel.BaseAction
import com.sajjad.base.presentation.viewmodel.BaseViewModel
import com.sajjad.base.presentation.viewmodel.BaseViewState
import javax.inject.Inject

internal class LoginViewModel @Inject constructor() :
    BaseViewModel<LoginViewModel.State, LoginViewModel.Action>(State()) {

    override fun onReduceState(viewAction: Action): State = state.copy(

    )

    sealed class Action : BaseAction {

    }

    data class State(
        val isLoading: Boolean = false
    ) : BaseViewState
}