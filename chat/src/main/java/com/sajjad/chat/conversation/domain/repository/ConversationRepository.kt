package com.sajjad.chat.conversation.domain.repository

import com.sajjad.chat.conversation.domain.model.Conversation

interface ConversationRepository {
    suspend fun getConversations(): List<Conversation>
}