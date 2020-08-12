package com.sajjad.auth.profile

import com.sajjad.auth.profile.presentation.ProfileFragment
import com.sajjad.auth.profile.presentation.ViewModelBinder
import com.sajjad.base.session.SessionComponent
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ProfileScope

@ProfileScope
@Component(
    modules = [ViewModelBinder::class],
    dependencies = [SessionComponent::class]
)
internal interface ProfileComponent {

    fun inject(profileFragment: ProfileFragment)

    @Component.Factory
    interface Factory {
        fun create(
            sessionComponent: SessionComponent
        ): ProfileComponent
    }
}