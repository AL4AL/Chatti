package com.sajjad.chat.chat.domain.usecase

import com.parse.ParseException
import com.sajjad.base.domain.UseCase
import com.sajjad.base.domain.model.Message
import com.sajjad.chat.chat.domain.repository.MessageRepository
import javax.inject.Inject

internal class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) : UseCase<SendMessageUseCase.RequestValues, SendMessageUseCase.Result>() {

    override suspend fun execute(requestValue: RequestValues): Result = try {
        val message = messageRepository.sendMessage(
            requestValue.contactUsername,
            requestValue.text
        )
        Result.Success(message)
    } catch (e: ParseException) {
        Result.Failure(e.code)
    } catch (e: Exception) {
        Result.Failure(ParseException.OTHER_CAUSE)
    }

    data class RequestValues(
        val contactUsername: String,
        val text: String
    ) : UseCase.RequestValue

    sealed class Result : UseCase.ResponseValue {
        data class Success(val message: Message) : Result()
        data class Failure(val errorCode: Int) : Result()
    }

}
