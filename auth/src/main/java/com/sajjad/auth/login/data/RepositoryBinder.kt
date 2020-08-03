package com.sajjad.auth.login.data

import com.sajjad.auth.login.domain.repository.LoginRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryBinder {

    @Binds
    abstract fun bindLoginRepository(loginRepository: ParseLoginRepository): LoginRepository
}