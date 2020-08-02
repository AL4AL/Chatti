package com.sajjad.chatti.application

import android.app.Application
import com.parse.Parse
import com.sajjad.application_component.ApplicationComponent
import com.sajjad.application_component.ApplicationComponentProvider
import com.sajjad.application_component.DaggerApplicationComponent
import com.sajjad.application_component.context.component.DaggerContextComponent
import com.sajjad.application_component.network.component.DaggerNetworkComponent
import com.sajjad.application_component.parse.component.DaggerParseComponent
import timber.log.Timber
import javax.inject.Inject

class ChattiApplication : Application(), ApplicationComponentProvider {

    private val contextComponent = DaggerContextComponent.factory()
        .create(applicationContext = this)

    private val networkComponent = DaggerNetworkComponent.factory()
        .create()

    private val parseComponent = DaggerParseComponent.factory()
        .create(
            contextComponent = this.contextComponent,
            networkComponent = this.networkComponent
        )

    override val applicationComponent: ApplicationComponent = DaggerApplicationComponent
        .factory()
        .create(
            contextComponent = this.contextComponent,
            parseComponent = this.parseComponent
        )

    @Inject
    lateinit var parseConfiguration: Parse.Configuration

    override fun onCreate() {
        super.onCreate()
        DaggerChattiApplicationComponent.factory()
            .create(
                applicationComponent = this.applicationComponent,
                parseComponent = this.applicationComponent.provideParseComponent()
            )
            .inject(this)
    }

    @Inject
    fun initParse() {
        Parse.initialize(this.parseConfiguration)
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE)
        Timber.v("Parse initialized")
    }
}