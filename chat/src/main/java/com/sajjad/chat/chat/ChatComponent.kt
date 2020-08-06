package com.sajjad.chat.chat

import com.sajjad.chat.chat.data.PageSize
import com.sajjad.chat.chat.data.RepositoryBinder
import com.sajjad.chat.chat.presentation.ChatFragment
import com.sajjad.chat.chat.presentation.ViewModelBinder
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope


@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ChatScope

@ChatScope
@Component(
    modules = [RepositoryBinder::class, ViewModelBinder::class]
)
internal interface ChatComponent {

    fun inject(chatFragment: ChatFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @PageSize
            @BindsInstance
            pageSize: Int
        ): ChatComponent
    }
}