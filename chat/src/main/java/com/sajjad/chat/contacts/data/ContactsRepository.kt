package com.sajjad.chat.contacts.data

import android.content.ContentResolver
import android.provider.ContactsContract
import com.example.base.domain.model.User
import com.example.base.formatPhoneNumber
import com.parse.ParseUser
import com.parse.ktx.findAll
import com.sajjad.chat.contacts.domain.model.Contact
import com.sajjad.chat.contacts.domain.repository.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactsRepository @Inject constructor(
    private val contentResolver: ContentResolver
) : ContactsRepository {
    override suspend fun getAllContacts(): List<Contact> = withContext(Dispatchers.IO) {
        val query = ParseUser.getQuery()
        query.whereContainedIn(User.PHONE_NUMBER, getPhoneNumbers())
        val contacts: MutableList<Contact> = ArrayList()
        query.findAll().forEach { user ->
            (user as User).phoneNumber?.let { phoneNumber ->
                if (phoneNumber != (ParseUser.getCurrentUser() as User).phoneNumber) {
                    contacts.add(Contact(user.username, phoneNumber))
                }
            }
        }
        contacts
    }

    private fun getPhoneNumbers(): List<String> {
        val projection = arrayOf(
            ContactsContract.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val cursor = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        if (cursor == null || cursor.count < 1) return emptyList()

        val phoneNumberColumnIndex =
            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val mimeTypeColumnIndex = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE)
        val phoneNumbers: MutableList<String> = ArrayList(cursor.count)
        while (cursor.moveToNext()) {
            if (cursor.getString(mimeTypeColumnIndex) == ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE) {
                val phoneNumber = cursor.getString(phoneNumberColumnIndex).formatPhoneNumber()
                phoneNumbers.add(phoneNumber)
            }
        }
        return phoneNumbers
    }

}