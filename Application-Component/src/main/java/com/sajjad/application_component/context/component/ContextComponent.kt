package com.sajjad.application_component.context.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ContextScope

@Component
@ContextScope
interface ContextComponent {

    @ApplicationContext
    fun provideApplicationContext(): Context

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance
            @ApplicationContext
            applicationContext: Context
        ): ContextComponent
    }
}