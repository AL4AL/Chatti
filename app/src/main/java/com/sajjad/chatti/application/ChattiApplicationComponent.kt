package com.sajjad.chatti.application

import com.sajjad.application_component.ApplicationComponent
import com.sajjad.application_component.parse.component.ParseComponent
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ChattiApplicationScope

@Component(
    dependencies = [ApplicationComponent::class, ParseComponent::class]
)
@ChattiApplicationScope
interface ChattiApplicationComponent {
    fun inject(application: ChattiApplication)

    @Component.Factory
    interface Factory {
        fun create(
            applicationComponent: ApplicationComponent,
            parseComponent: ParseComponent
        ): ChattiApplicationComponent
    }
}