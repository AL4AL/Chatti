package com.sajjad.base.domain

abstract class UseCase<Request : UseCase.RequestValue, Response : UseCase.ResponseValue> {

    lateinit var requestValue: Request

    protected abstract suspend fun execute(requestValue: Request): Response

    suspend fun run(): Response {
        return execute(requestValue)
    }

    interface RequestValue
    interface ResponseValue
}