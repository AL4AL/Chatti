package com.sajjad.auth.login

import com.sajjad.auth.login.data.RepositoryBinder
import com.sajjad.auth.login.presentation.LoginFragment
import com.sajjad.auth.login.presentation.ViewModelBinder
import dagger.Component

@Component(
    modules = [ViewModelBinder::class, RepositoryBinder::class]
)
internal interface LoginComponent {

    fun inject(loginFragment: LoginFragment)

    @Component.Factory
    interface Factory {
        fun create(): LoginComponent
    }
}