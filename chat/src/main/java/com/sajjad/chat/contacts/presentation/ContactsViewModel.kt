package com.sajjad.chat.contacts.presentation

import androidx.lifecycle.viewModelScope
import com.sajjad.base.domain.UseCaseHandler
import com.sajjad.base.presentation.viewmodel.BaseAction
import com.sajjad.base.presentation.viewmodel.BaseViewModel
import com.sajjad.base.presentation.viewmodel.BaseViewState
import com.sajjad.chat.contacts.domain.model.Contact
import com.sajjad.chat.contacts.domain.usecase.LoadContactsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class ContactsViewModel @Inject constructor(
    private val useCaseHandler: UseCaseHandler,
    private val loadContactsUseCase: LoadContactsUseCase
) : BaseViewModel<ContactsViewModel.ViewState, ContactsViewModel.Action>(
    ViewState()
) {

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        is Action.LoadingContacts -> state.copy(
            isLoading = true,
            isError = false,
            contacts = null
        )
        is Action.ContactsLoaded -> state.copy(
            isLoading = false,
            isError = false,
            contacts = viewAction.contacts
        )
        is Action.ErrorLoading -> state.copy(
            isLoading = false,
            isError = true,
            contacts = null
        )
    }

    fun loadContacts() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            sendAction(Action.ErrorLoading)
            Timber.e(throwable)
        }

        viewModelScope.launch(exceptionHandler) {
            sendAction(Action.LoadingContacts)
            val result = useCaseHandler.execute(
                loadContactsUseCase,
                LoadContactsUseCase.RequestValues
            )
            when (result) {
                is LoadContactsUseCase.Result.Success -> {
                    sendAction(
                        Action.ContactsLoaded(
                            result.contacts
                        )
                    )
                }
                is LoadContactsUseCase.Result.Error -> {
                    sendAction(Action.ErrorLoading)
                }
            }
        }
    }

    internal sealed class Action : BaseAction {
        object LoadingContacts : Action()
        data class ContactsLoaded(val contacts: List<Contact>) : Action()
        object ErrorLoading : Action()
    }

    internal data class ViewState(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val contacts: List<Contact>? = null
    ) : BaseViewState
}