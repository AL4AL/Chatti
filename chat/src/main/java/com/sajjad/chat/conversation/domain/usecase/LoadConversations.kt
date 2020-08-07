package com.sajjad.chat.conversation.domain.usecase

import com.parse.ParseException
import com.sajjad.base.domain.UseCase
import com.sajjad.chat.conversation.domain.model.Conversation
import com.sajjad.chat.conversation.domain.repository.ConversationRepository
import javax.inject.Inject

class LoadConversations @Inject constructor(
    private val conversationRepository: ConversationRepository
) : UseCase<LoadConversations.RequestValues, LoadConversations.Result>() {

    override suspend fun execute(requestValue: RequestValues): Result = try {
        Result.Success(
            conversationRepository.getConversations()
        )
    } catch (e: ParseException) {
        Result.Failure(e.code)
    } catch (e: Exception) {
        Result.Failure(ParseException.OTHER_CAUSE)
    }


    object RequestValues : UseCase.RequestValue

    sealed class Result : UseCase.ResponseValue {
        data class Success(val conversations: List<Conversation>) : Result()
        data class Failure(val errorCode: Int) : Result()
    }
}