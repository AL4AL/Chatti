package com.sajjad.chat.conversation.presentation.di

import com.sajjad.chat.conversation.presentation.ConversationsFragment
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ConversationsPresentationScope

@Component(
    modules = [ViewModelBinder::class]
)
@ConversationsPresentationScope
internal interface ConversationsPresentationComponent {
    fun inject(conversationsFragment: ConversationsFragment)
}