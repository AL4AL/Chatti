package com.sajjad.application_component.network.component

import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class NetworkScope

@Component(
    modules = [NetworkModule::class]
)
@NetworkScope
interface NetworkComponent {
    fun provideOkHttpBuilder(): OkHttpClient.Builder
    fun provideOkHttpClient(): OkHttpClient

    @Component.Factory
    interface Factory {
        fun create(): NetworkComponent
    }
}