package com.sajjad.chat.chat.domain.usecase

import com.parse.ParseException
import com.sajjad.base.domain.UseCase
import com.sajjad.base.domain.model.Message
import com.sajjad.chat.chat.data.ParseMessageRepository
import com.sajjad.chat.chat.domain.repository.MessageRepository
import javax.inject.Inject

internal class MessageLiveListenerUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) : UseCase<MessageLiveListenerUseCase.RequestValues, MessageLiveListenerUseCase.Result>() {

    override suspend fun execute(requestValue: RequestValues): Result = try {

        Result.Success.Subscription(
            messageRepository.listenToNewMessages(
                requestValue.contactUsername,
                requestValue.onResult
            )
        )
    } catch (e: ParseException) {
        Result.Failure(e.code)
    } catch (e: Exception) {
        Result.Failure(ParseException.OTHER_CAUSE)
    }

    data class RequestValues(
        val contactUsername: String,
        val onResult: (result: Result) -> Unit
    ) : UseCase.RequestValue

    sealed class Result : UseCase.ResponseValue {
        sealed class Success : Result() {
            data class NewMessage(val message: Message) : Success()
            data class Subscription(val liveQuery: ParseMessageRepository.Disconnectable) :
                Success()
        }

        data class Failure(val errorCode: Int) : Result()
    }
}