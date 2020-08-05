package com.sajjad.chat.contacts.data

import dagger.Binds
import dagger.Module

private typealias IContactsRepository = com.sajjad.chat.contacts.domain.repository.ContactsRepository

@Module
abstract class RepositoryBinder {

    @Binds
    abstract fun bindContactsRepository(contactsRepository: ContactsRepository): IContactsRepository
}