package com.sajjad.base.session

import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class SessionScope

@Component
@SessionScope
interface SessionComponent {
    fun provideSessionHolder(): SessionHolder

    @Component.Factory
    interface Factory {
        fun create(): SessionComponent
    }
}