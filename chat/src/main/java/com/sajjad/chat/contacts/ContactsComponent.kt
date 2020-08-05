package com.sajjad.chat.contacts

import android.content.ContentResolver
import com.sajjad.chat.contacts.data.RepositoryBinder
import com.sajjad.chat.contacts.presentation.ContactsFragment
import com.sajjad.chat.contacts.presentation.ViewModelBinder
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ContactsScope

@Component(
    modules = [RepositoryBinder::class, ViewModelBinder::class]
)
@ContactsScope
internal interface ContactsComponent {
    fun inject(contactsFragment: ContactsFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            contentResolver: ContentResolver
        ): ContactsComponent
    }
}