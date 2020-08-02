package com.sajjad.application_component.parse.component

import com.parse.Parse
import com.sajjad.application_component.context.component.ContextComponent
import com.sajjad.application_component.network.component.NetworkComponent
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ParseScope

@ParseScope
@Component(
    modules = [ParseModule::class],
    dependencies = [ContextComponent::class, NetworkComponent::class]
)
interface ParseComponent {

    fun provideParseConfiguration(): Parse.Configuration

    @Component.Factory
    interface Factory {
        fun create(
            contextComponent: ContextComponent,
            networkComponent: NetworkComponent
        ): ParseComponent
    }
}