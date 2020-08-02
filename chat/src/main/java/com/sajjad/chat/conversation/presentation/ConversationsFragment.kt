package com.sajjad.chat.conversation.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sajjad.base.presentation.BaseFragment
import com.sajjad.base.presentation.observe
import com.sajjad.chat.conversation.domain.Conversation
import com.sajjad.chat.conversation.presentation.di.DaggerConversationsPresentationComponent
import com.sajjad.chat.databinding.FragConversationsBinding
import kotlinx.android.synthetic.main.frag_conversations.*
import javax.inject.Inject

class ConversationsFragment @Inject constructor() : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var fragmentBinding: FragConversationsBinding

    private val conversationViewModel: ConversationViewModel
            by viewModels(factoryProducer = { viewModelFactory })
    private val authenticationViewModel: AuthenticationViewModel
            by viewModels(factoryProducer = { viewModelFactory })

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.parentContext = context
        DaggerConversationsPresentationComponent.create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBinding = FragConversationsBinding.inflate(inflater)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFabOnClickListener()
        observeAuthState()
        observeConversationsState()
    }

    private fun setFabOnClickListener() {
        fragmentBinding.floatingActionButton.setOnClickListener {
            // TODO Go to contacts Fragment
        }
    }

    private fun observeAuthState() {
        authenticationViewModel.stateLiveData
            .observe(viewLifecycleOwner) {
                if (it === null) return@observe
                loadingSate(it.isLoading)
                authenticatedState(it.authenticated)
            }
    }

    private fun authenticatedState(isAuthenticated: Boolean) {
        if (isAuthenticated) {
            conversationViewModel.loadConversations()
        } else {
            findNavController().popBackStack()
            // TODO Go to login fragment
        }
    }

    private fun observeConversationsState() {
        conversationViewModel.stateLiveData
            .observe(viewLifecycleOwner) {
                if (it == null) return@observe
                loadingSate(it.isLoading)
                errorLoadingConversationsList(it.isErrorLoading)
                conversationLoadState(it.conversations)
            }
    }

    private fun loadingSate(isLoading: Boolean = true) {
        progressBar.isVisible = isLoading
    }

    private fun errorLoadingConversationsList(isError: Boolean = true) {
        fragmentBinding.retryButton.isVisible = isError
    }

    private fun conversationLoadState(conversations: List<Conversation>?) {
        // TODO() Show conversations
    }
}