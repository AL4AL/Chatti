package com.sajjad.chat.chat.presentation

import androidx.lifecycle.viewModelScope
import com.sajjad.base.domain.UseCaseHandler
import com.sajjad.base.domain.model.Message
import com.sajjad.base.presentation.viewmodel.BaseAction
import com.sajjad.base.presentation.viewmodel.BaseViewModel
import com.sajjad.base.presentation.viewmodel.BaseViewState
import com.sajjad.chat.chat.domain.usecase.LoadMessageUseCase
import com.sajjad.chat.chat.domain.usecase.LoadMessageUseCase.Result
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class ChatViewModel @Inject constructor(
    private val useCaseHandler: UseCaseHandler,
    private val loadMessageUseCase: LoadMessageUseCase
) : BaseViewModel<ChatViewModel.State, ChatViewModel.Action>(State()) {

    override fun onReduceState(viewAction: Action): State = state.copy(
        isLoading = viewAction is Action.Loading,
        isError = viewAction is Action.Error,
        messages = if (viewAction is Action.Success) {
            viewAction.messages
        } else null
    )

    fun nextPage(contactUsername: String) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            sendAction(Action.Error)
            Timber.e(throwable)
        }
        viewModelScope.launch(exceptionHandler) {
            sendAction(Action.Loading)
            val result = useCaseHandler.execute(
                loadMessageUseCase,
                LoadMessageUseCase.RequestValues(
                    contactUsername,
                    true
                )
            )
            when (result) {
                is Result.Success -> sendAction(Action.Success(result.messages))
                is Result.Failure -> sendAction(Action.Error)
            }
        }
    }

    fun reload(contactUsername: String) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            sendAction(Action.Error)
            Timber.e(throwable)
        }
        viewModelScope.launch(exceptionHandler) {
            sendAction(Action.Loading)
            val result = useCaseHandler.execute(
                loadMessageUseCase,
                LoadMessageUseCase.RequestValues(
                    contactUsername,
                    false
                )
            )
            when (result) {
                is Result.Success -> sendAction(Action.Success(result.messages))
                is Result.Failure -> sendAction(Action.Error)
            }
        }
    }

    sealed class Action : BaseAction {
        object Loading : Action()
        object Error : Action()
        data class Success(val messages: List<Message>) : Action()
    }

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val messages: List<Message>? = null
    ) : BaseViewState
}