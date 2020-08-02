package com.sajjad.application_component.parse.component

import android.content.Context
import com.parse.Parse
import com.sajjad.application_component.context.component.ApplicationContext
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER
)
internal annotation class ParseApplicationId

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER
)
internal annotation class ParseServerUrl

@Module
internal object ParseModule {

    @JvmStatic
    @Provides
    @ParseScope
    fun provideParseConfiguration(
        @ApplicationContext context: Context,
        @ParseApplicationId id: String,
        @ParseServerUrl url: String,
        okHttpClientBuilder: OkHttpClient.Builder
    ): Parse.Configuration {
        return Parse.Configuration.Builder(context)
            .clientBuilder(okHttpClientBuilder)
            .enableLocalDataStore()
            .applicationId(id)
            .server(url)
            .build()
    }

    @JvmStatic
    @Provides
    @ParseApplicationId
    fun provideParseApplicationId() = PARSE_APPLICATION_ID

    @JvmStatic
    @Provides
    @ParseServerUrl
    fun provideParseServerUrl() = SERVER_URL
}