package com.sajjad.chat.contacts.domain.usecase

import com.parse.ParseException
import com.sajjad.base.domain.UseCase
import com.sajjad.chat.contacts.domain.model.Contact
import com.sajjad.chat.contacts.domain.repository.ContactsRepository
import javax.inject.Inject

internal class LoadContactsUseCase @Inject constructor(
    private val contactsRepository: ContactsRepository
) : UseCase<LoadContactsUseCase.RequestValues, LoadContactsUseCase.Result>() {

    override suspend fun execute(requestValue: RequestValues): Result = try {
        val result = contactsRepository.getAllContacts()
        Result.Success(result)
    } catch (e: Exception) {
        when (e) {
            is ParseException -> Result.Error(e.code)
            else -> Result.Error(-1)
        }
    }


    internal object RequestValues : UseCase.RequestValue

    internal sealed class Result : UseCase.ResponseValue {
        data class Success(val contacts: List<Contact>) : Result()
        data class Error(val errorCode: Int) : Result()
    }
}