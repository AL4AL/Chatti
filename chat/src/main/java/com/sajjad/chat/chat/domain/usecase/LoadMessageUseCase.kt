package com.sajjad.chat.chat.domain.usecase

import com.parse.ParseException
import com.sajjad.base.domain.UseCase
import com.sajjad.base.domain.model.Message
import com.sajjad.chat.chat.domain.repository.MessageRepository
import javax.inject.Inject

internal class LoadMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) : UseCase<LoadMessageUseCase.RequestValues, LoadMessageUseCase.Result>() {

    override suspend fun execute(requestValue: RequestValues): Result = try {
        val messages = messageRepository.getMessages(
            requestValue.contactUsername,
            requestValue.page
        )
        Result.Success(messages)
    } catch (e: ParseException) {
        Result.Failure(e.code)
    } catch (e: Exception) {
        Result.Failure(ParseException.OTHER_CAUSE)
    }


    data class RequestValues(
        val contactUsername: String,
        val page: Int
    ) : UseCase.RequestValue

    sealed class Result : UseCase.ResponseValue {
        data class Success(val messages: List<Message>) : Result()
        data class Failure(val errorCode: Int) : Result()
    }

}