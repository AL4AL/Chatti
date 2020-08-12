package com.sajjad.auth.profile.presentation

import com.sajjad.base.domain.UseCaseHandler
import com.sajjad.base.presentation.viewmodel.BaseAction
import com.sajjad.base.presentation.viewmodel.BaseViewModel
import com.sajjad.base.presentation.viewmodel.BaseViewState
import javax.inject.Inject

internal class ProfileViewModel @Inject constructor(
    private val useCaseHandler: UseCaseHandler
) : BaseViewModel<ProfileViewModel.State, ProfileViewModel.Action>(State()) {

    data class State(
        val loggingOut: Boolean = false
    ) : BaseViewState

    sealed class Action : BaseAction

    override fun onReduceState(viewAction: Action): State = state.copy(
        // TODO
    )

    fun logOut() {
        // TODO
    }
}