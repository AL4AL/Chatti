package com.sajjad.auth.signup

import com.sajjad.auth.signup.data.RepositoryBinder
import com.sajjad.auth.signup.presentation.SignUpFragment
import com.sajjad.auth.signup.presentation.ViewModelBinder
import com.sajjad.base.session.SessionComponent
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class SignUpScope

@Component(
    modules = [RepositoryBinder::class, ViewModelBinder::class],
    dependencies = [SessionComponent::class]
)
@SignUpScope
internal interface SignUpComponent {

    fun inject(signUpFragment: SignUpFragment)

    @Component.Factory
    interface Factory {
        fun create(
            sessionComponent: SessionComponent
        ): SignUpComponent
    }
}