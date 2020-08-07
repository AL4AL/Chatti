package com.sajjad.chat.chat.domain.repository

import com.sajjad.base.domain.model.Message
import com.sajjad.chat.chat.data.ParseMessageRepository
import com.sajjad.chat.chat.domain.usecase.MessageLiveListenerUseCase

internal interface MessageRepository {
    suspend fun getMessages(contactUsername: String, page: Int): List<Message>
    suspend fun sendMessage(contactUsername: String, text: String): Message
    suspend fun listenToNewMessages(
        contactUsername: String,
        onResult: (result: MessageLiveListenerUseCase.Result) -> Unit
    ): ParseMessageRepository.Disconnectable
}