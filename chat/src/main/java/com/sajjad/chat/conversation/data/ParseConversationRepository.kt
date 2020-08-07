package com.sajjad.chat.conversation.data

import com.example.base.domain.model.User
import com.parse.ParseQuery
import com.parse.ParseUser
import com.parse.ktx.findAll
import com.sajjad.base.domain.model.Message
import com.sajjad.chat.conversation.domain.model.Conversation
import com.sajjad.chat.conversation.domain.repository.ConversationRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ParseConversationRepository @Inject constructor() : ConversationRepository {
    override suspend fun getConversations(): List<Conversation> = withContext(IO) {
        val allUsers = ParseQuery.getQuery(User::class.java)

        val messagesFromMeToThem = ParseQuery.getQuery(Message::class.java)
            .whereEqualTo(Message.FROM_USERNAME, ParseUser.getCurrentUser().username)
            .whereMatchesKeyInQuery(Message.TO_USERNAME, User.USERNAME, allUsers)

        val usersThatIMessagedThem = ParseQuery.getQuery(User::class.java)
            .whereMatchesKeyInQuery(User.USERNAME, Message.TO_USERNAME, messagesFromMeToThem)


        val messagesFromThemToMe = ParseQuery.getQuery(Message::class.java)
            .whereEqualTo(Message.TO_USERNAME, ParseUser.getCurrentUser().username)
            .whereMatchesKeyInQuery(Message.FROM_USERNAME, User.USERNAME, allUsers)

        val usersThatTheyMessagedMe = ParseQuery.getQuery(User::class.java)
            .whereMatchesKeyInQuery(User.USERNAME, Message.FROM_USERNAME, messagesFromThemToMe)

        ParseQuery.or(listOf(usersThatIMessagedThem, usersThatTheyMessagedMe))
            .findAll()
            .map {
                Conversation(
                    it.username
                )
            }
    }
}