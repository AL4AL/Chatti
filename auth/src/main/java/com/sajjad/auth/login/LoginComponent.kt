package com.sajjad.auth.login

import com.sajjad.auth.login.data.RepositoryBinder
import com.sajjad.auth.login.presentation.LoginFragment
import com.sajjad.auth.login.presentation.ViewModelBinder
import com.sajjad.base.session.SessionComponent
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class LoginScope

@Component(
    modules = [ViewModelBinder::class, RepositoryBinder::class],
    dependencies = [SessionComponent::class]
)
@LoginScope
internal interface LoginComponent {

    fun inject(loginFragment: LoginFragment)

    @Component.Factory
    interface Factory {
        fun create(
            sessionComponent: SessionComponent
        ): LoginComponent
    }
}