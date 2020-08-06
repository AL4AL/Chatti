package com.sajjad.chat.chat.presentation

import androidx.lifecycle.viewModelScope
import com.sajjad.base.domain.UseCaseHandler
import com.sajjad.base.domain.model.Message
import com.sajjad.base.presentation.viewmodel.BaseAction
import com.sajjad.base.presentation.viewmodel.BaseViewModel
import com.sajjad.base.presentation.viewmodel.BaseViewState
import com.sajjad.chat.chat.domain.usecase.SendMessageUseCase
import com.sajjad.chat.chat.domain.usecase.SendMessageUseCase.Result
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class EditorViewModel @Inject constructor(
    private val useCaseHandler: UseCaseHandler,
    private val sendMessageUseCase: SendMessageUseCase
) : BaseViewModel<EditorViewModel.State, EditorViewModel.Action>(State()) {

    override fun onReduceState(viewAction: Action): State = state.copy(
        sendingMessage = viewAction is Action.SendingMessage,
        messageSent = if (viewAction is Action.SendMessageSuccessful) viewAction.message else null,
        sendingFailed = viewAction is Action.SendMessageFailed
    )

    fun sendMessage(text: String, to: String) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            sendAction(Action.SendMessageFailed)
            Timber.e(throwable)
        }
        viewModelScope.launch(exceptionHandler) {
            val result = useCaseHandler.execute(
                sendMessageUseCase,
                SendMessageUseCase.RequestValues(
                    to,
                    text
                )
            )
            when (result) {
                is Result.Success -> sendAction(Action.SendMessageSuccessful(result.message))
                is Result.Failure -> sendAction(Action.SendMessageFailed)
            }
        }
    }

    sealed class Action : BaseAction {
        object SendingMessage : Action()
        object SendMessageFailed : Action()
        data class SendMessageSuccessful(val message: Message) : Action()
    }

    data class State(
        val sendingMessage: Boolean = false,
        val messageSent: Message? = null,
        val sendingFailed: Boolean = false
    ) : BaseViewState
}