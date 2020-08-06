package com.sajjad.chat.chat.data

import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import com.parse.ktx.findAll
import com.sajjad.base.domain.model.Message
import com.sajjad.chat.chat.domain.repository.MessageRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ParseMessageRepository @Inject constructor(
    @PageSize
    private val pageSize: Int
) : MessageRepository {

    override suspend fun getMessages(
        contactId: String,
        page: Int
    ): List<Message> = withContext<List<Message>>(IO) {
        val meToHim = ParseQuery.getQuery(Message::class.java)
            .whereEqualTo(Message.FROM_USERNAME, ParseUser.getCurrentUser().username)
            .whereEqualTo(Message.TO_USERNAME, contactId)
        val himToMe = ParseQuery.getQuery(Message::class.java)
            .whereEqualTo(Message.FROM_USERNAME, contactId)
            .whereEqualTo(Message.TO_USERNAME, ParseUser.getCurrentUser().username)
        val result = ParseQuery.or(listOf(meToHim, himToMe))
            .orderByDescending(ParseObject.KEY_CREATED_AT)
            .setSkip(calculateOffset(pageSize, page))
            .setLimit(pageSize)
            .findAll()
        return@withContext result
    }

    override suspend fun sendMessage(
        contactUsername: String,
        text: String
    ) = withContext(IO) {
        return@withContext Message().apply {
            this.text = text
            this.to = contactUsername
            this.from = ParseUser.getCurrentUser().username
            save()
        }
    }

    private fun calculateOffset(limit: Int, page: Int): Int {
        return (limit * page) - limit
    }
}