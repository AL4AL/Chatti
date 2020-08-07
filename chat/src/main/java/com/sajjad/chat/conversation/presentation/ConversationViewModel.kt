package com.sajjad.chat.conversation.presentation

import androidx.lifecycle.viewModelScope
import com.sajjad.base.domain.UseCaseHandler
import com.sajjad.base.presentation.viewmodel.BaseAction
import com.sajjad.base.presentation.viewmodel.BaseViewModel
import com.sajjad.base.presentation.viewmodel.BaseViewState
import com.sajjad.chat.conversation.domain.model.Conversation
import com.sajjad.chat.conversation.domain.usecase.LoadConversations
import com.sajjad.chat.conversation.domain.usecase.LoadConversations.Result
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class ConversationViewModel @Inject constructor(
    private val useCaseHandler: UseCaseHandler,
    private val loadConversations: LoadConversations
) : BaseViewModel<ConversationViewModel.ViewState, ConversationViewModel.Action>(ViewState()) {

    override fun onReduceState(viewAction: Action): ViewState = state.copy(
        isLoading = viewAction is Action.Loading,
        isErrorLoading = viewAction is Action.LoadingFailed,
        conversations = if (viewAction is Action.ConversationsLoaded) viewAction.conversations else null
    )

    fun loadConversations() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            sendAction(Action.LoadingFailed)
            Timber.e(throwable)
        }
        viewModelScope.launch(exceptionHandler) {
            sendAction(Action.Loading)
            val result = useCaseHandler.execute(
                loadConversations,
                LoadConversations.RequestValues
            )
            when (result) {
                is Result.Success -> sendAction(Action.ConversationsLoaded(result.conversations))
                is Result.Failure -> sendAction(Action.LoadingFailed)
            }
        }
    }

    internal data class ViewState(
        val isLoading: Boolean = false,
        val isErrorLoading: Boolean = false,
        val conversations: List<Conversation>? = null
    ) : BaseViewState

    internal sealed class Action : BaseAction {
        object Loading : Action()
        data class ConversationsLoaded(val conversations: List<Conversation>) : Action()
        object LoadingFailed : Action()
    }
}