package com.sajjad.auth.signup.domain.usecase

import com.parse.ParseException
import com.sajjad.auth.signup.domain.repository.SignUpRepository
import com.sajjad.base.domain.UseCase
import javax.inject.Inject

internal class SignUpUseCase @Inject constructor(
    private val signUpRepository: SignUpRepository
) : UseCase<SignUpUseCase.RequestValues, SignUpUseCase.Result>() {

    override suspend fun execute(requestValue: RequestValues): Result = try {
        signUpRepository.signUp(
            requestValue.username,
            requestValue.password,
            requestValue.phoneNumber
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

    internal data class RequestValues(
        val username: String,
        val password: String,
        val phoneNumber: String
    ) : UseCase.RequestValue
}