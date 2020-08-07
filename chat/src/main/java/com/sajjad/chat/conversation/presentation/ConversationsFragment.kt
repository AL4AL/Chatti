package com.sajjad.chat.conversation.presentation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.sajjad.base.presentation.BaseFragment
import com.sajjad.base.presentation.observe
import com.sajjad.chat.R
import com.sajjad.chat.conversation.DaggerConversationsComponent
import com.sajjad.chat.conversation.domain.model.Conversation
import com.sajjad.chat.conversation.presentation.adapter.ConversationAdapter
import com.sajjad.chat.databinding.FragConversationsBinding
import kotlinx.android.synthetic.main.frag_conversations.*
import javax.inject.Inject

internal class ConversationsFragment @Inject constructor() : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var conversationAdapter: ConversationAdapter

    private lateinit var fragmentBinding: FragConversationsBinding

    private val conversationViewModel: ConversationViewModel
            by viewModels(factoryProducer = { viewModelFactory })
    private val authenticationViewModel: AuthenticationViewModel
            by viewModels(factoryProducer = { viewModelFactory })

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.parentContext = context
        DaggerConversationsComponent.create()
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
        authenticationViewModel.authenticate()
        setFabOnClickListener()
        observeAuthState()
        observeConversationsState()
        setUpConversationsRecyclerView()
    }

    private fun setFabOnClickListener() {
        fragmentBinding.floatingActionButton.setOnClickListener {
            findNavController().navigate(
                ConversationsFragmentDirections.actionConversationsFragmentToContactsFragment()
            )
        }
    }

    private fun setUpConversationsRecyclerView() {
        fragmentBinding.conversationsRV.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(parentContext, VERTICAL, false)
            adapter = this@ConversationsFragment.conversationAdapter
            addItemDecoration(DividerItemDecoration(parentContext, VERTICAL))
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

    private fun authenticatedState(isAuthenticated: Boolean?) {
        if (isAuthenticated === null) return
        if (isAuthenticated) {
            conversationViewModel.loadConversations()
        } else {
            findNavController().popBackStack()
            findNavController().navigate(
                Uri.parse(getString(R.string.login_deep_link))
            )
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
        if (conversations !== null) {
            if (conversations.isEmpty()) {
                emptyState()
            } else {
                emptyState(isEmpty = false)
                updateAdapter(conversations)
            }
        }
    }

    private fun emptyState(isEmpty: Boolean = true) {
        fragmentBinding.emptyStateImage.isVisible = isEmpty
        fragmentBinding.emptyStateText.isVisible = isEmpty
    }

    private fun updateAdapter(conversations: List<Conversation>) {
        conversationAdapter.conversations = conversations
        conversationAdapter.onItemClickListener = { _, conversation ->
            conversation?.let {
                val action = ConversationsFragmentDirections
                    .actionConversationsFragmentToChatFragment(conversation.username)
                findNavController().navigate(action)
            }
        }
    }
}