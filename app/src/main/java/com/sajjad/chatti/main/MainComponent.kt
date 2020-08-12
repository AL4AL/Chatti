package com.sajjad.chatti.main

import com.sajjad.base.session.SessionComponent
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class MainScope

@Component(
    dependencies = [SessionComponent::class]
)
@MainScope
internal interface MainComponent {
    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            sessionComponent: SessionComponent
        ): MainComponent
    }
}