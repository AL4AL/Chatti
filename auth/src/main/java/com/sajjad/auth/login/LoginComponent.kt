package com.sajjad.auth.login

import com.sajjad.auth.login.presentation.LoginFragment
import com.sajjad.auth.login.presentation.ViewModelBinder
import dagger.Component

@Component(
    modules = [ViewModelBinder::class]
)
internal interface LoginComponent {

    fun inject(loginFragment: LoginFragment)

    @Component.Factory
    interface Factory {
        fun create(): LoginComponent
    }
}