package com.sajjad.application_component

import com.sajjad.application_component.context.component.ContextComponent
import com.sajjad.application_component.parse.component.ParseComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ApplicationScope

@Component
@ApplicationScope
interface ApplicationComponent {

    fun provideContextComponent(): ContextComponent
    fun provideParseComponent(): ParseComponent

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            contextComponent: ContextComponent,
            @BindsInstance
            parseComponent: ParseComponent
        ): ApplicationComponent
    }
}