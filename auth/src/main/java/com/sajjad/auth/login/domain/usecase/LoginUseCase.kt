package com.sajjad.auth.login.domain.usecase

import com.parse.ParseException
import com.sajjad.auth.login.domain.repository.LoginRepository
import com.sajjad.base.domain.UseCase
import javax.inject.Inject

internal class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) : UseCase<LoginUseCase.RequestValue, LoginUseCase.Result>() {

    override suspend fun execute(requestValue: RequestValue): Result = try {
        loginRepository.login(
            requestValue.username,
            requestValue.password
        )
        Result.Success
    } catch (e: ParseException) {
        Result.Error(e.code)
    } catch (e: Exception) {
        Result.Error(ParseException.OTHER_CAUSE)
    }


    internal sealed class Result : UseCase.ResponseValue {
        object Success : Result()
        data class Error(val errorCode: Int) : Result()
    }

    internal data class RequestValue(
        val username: String,
        val password: String
    ) : UseCase.RequestValue
}