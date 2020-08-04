package com.sajjad.auth.signup

import com.sajjad.auth.signup.data.RepositoryBinder
import com.sajjad.auth.signup.presentation.SignUpFragment
import com.sajjad.auth.signup.presentation.ViewModelBinder
import dagger.Component

@Component(
    modules = [RepositoryBinder::class, ViewModelBinder::class]
)
internal interface SignUpComponent {

    fun inject(signUpFragment: SignUpFragment)

    @Component.Factory
    interface Factory {
        fun create(): SignUpComponent
    }
}