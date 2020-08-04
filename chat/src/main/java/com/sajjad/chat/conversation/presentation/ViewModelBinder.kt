package com.sajjad.chat.conversation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sajjad.base.di.ViewModelKey
import com.sajjad.base.presentation.factory.ViewModelProviderFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelBinder {

    @Binds
    abstract fun bindViewModelProviderFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ConversationViewModel::class)
    abstract fun bindConversationsViewModel(viewModel: ConversationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthenticationViewModel::class)
    abstract fun bindAuthViewModel(viewModel: AuthenticationViewModel): ViewModel
}