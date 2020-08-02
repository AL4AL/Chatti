package com.sajjad.chat.conversation.presentation

import androidx.lifecycle.viewModelScope
import com.sajjad.base.presentation.viewmodel.BaseAction
import com.sajjad.base.presentation.viewmodel.BaseViewModel
import com.sajjad.base.presentation.viewmodel.BaseViewState
import com.sajjad.chat.conversation.domain.Conversation
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class ConversationViewModel @Inject constructor() :
    BaseViewModel<ConversationViewModel.ViewState, ConversationViewModel.Action>(ViewState()) {

    override fun onReduceState(viewAction: Action): ViewState = state.copy(
        isLoading = viewAction is Action.Loading,
        isErrorLoading = viewAction is Action.LoadingFailed,
        conversations = if (viewAction is Action.ConversationsLoaded) viewAction.conversations else null
    )

    fun loadConversations() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Timber.e(throwable)
        }
        viewModelScope.launch(exceptionHandler) {
            sendAction(Action.Loading)
            // TODO load conversations
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