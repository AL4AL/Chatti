package com.sajjad.chat.conversation

import com.sajjad.chat.conversation.data.RepositoryBinder
import com.sajjad.chat.conversation.presentation.ConversationsFragment
import com.sajjad.chat.conversation.presentation.ViewModelBinder
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ConversationsScope

@Component(
    modules = [ViewModelBinder::class, RepositoryBinder::class]
)
@ConversationsScope
internal interface ConversationsComponent {
    fun inject(conversationsFragment: ConversationsFragment)
}