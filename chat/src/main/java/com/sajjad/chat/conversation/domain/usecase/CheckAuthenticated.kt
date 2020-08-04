package com.sajjad.chat.conversation.domain.usecase

import com.parse.ParseException
import com.sajjad.base.domain.UseCase
import com.sajjad.chat.conversation.domain.repository.AuthenticationRepository
import javax.inject.Inject

internal class CheckAuthenticated @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : UseCase<CheckAuthenticated.RequestValue, CheckAuthenticated.Result>() {

    override suspend fun execute(requestValue: RequestValue): Result = try {
        val result = authenticationRepository.isAuthenticated()
        Result.Success(result)
    } catch (e: ParseException) {
        Result.Failure(e.code)
    } catch (e: Exception) {
        Result.Failure(ParseException.OTHER_CAUSE)
    }

    object RequestValue : UseCase.RequestValue

    sealed class Result : UseCase.ResponseValue {
        data class Success(val authenticated: Boolean) : Result()
        data class Failure(val code: Int) : Result()
    }

}