package com.sajjad.chat.chat.data

import com.sajjad.chat.chat.domain.repository.MessageRepository
import dagger.Binds
import dagger.Module

@Module
internal abstract class RepositoryBinder {

    @Binds
    abstract fun bindMessageRepository(messageRepository: ParseMessageRepository): MessageRepository
}