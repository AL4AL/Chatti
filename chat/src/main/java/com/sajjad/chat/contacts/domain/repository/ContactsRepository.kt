package com.sajjad.chat.contacts.domain.repository

import com.sajjad.chat.contacts.domain.model.Contact

interface ContactsRepository {
    suspend fun getAllContacts(): List<Contact>
}