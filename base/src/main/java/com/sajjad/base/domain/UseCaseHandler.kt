package com.sajjad.base.domain

import javax.inject.Inject

class UseCaseHandler @Inject constructor() {

    suspend fun <RequestValue : UseCase.RequestValue, ResponseValue : UseCase.ResponseValue> execute(
        useCase: UseCase<RequestValue, ResponseValue>,
        requestValue: RequestValue
    ): ResponseValue {
        useCase.requestValue = requestValue
        return useCase.run()
    }

}