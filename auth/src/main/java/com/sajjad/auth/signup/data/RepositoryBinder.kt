package com.sajjad.auth.signup.data

import com.sajjad.auth.signup.domain.repository.SignUpRepository
import dagger.Binds
import dagger.Module

@Module
internal abstract class RepositoryBinder {

    @Binds
    abstract fun bindSignUpRepository(signUpRepository: ParseSignUpRepository): SignUpRepository
}