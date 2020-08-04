package com.sajjad.chat.conversation.data

import com.sajjad.chat.conversation.domain.repository.AuthenticationRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryBinder {

    @Binds
    abstract fun bindAuthRepository(authRepository: ParseAuthRepository): AuthenticationRepository
}