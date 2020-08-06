package com.sajjad.chat.chat.domain.repository

import com.sajjad.base.domain.model.Message

interface MessageRepository {
    suspend fun getMessages(contactId: String, page: Int): List<Message>
    suspend fun sendMessage(contactUsername: String, text: String): Message
}