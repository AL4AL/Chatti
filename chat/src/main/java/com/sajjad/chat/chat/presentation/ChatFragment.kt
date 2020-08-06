package com.sajjad.chat.chat.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sajjad.base.domain.model.Message
import com.sajjad.base.presentation.BaseFragment
import com.sajjad.base.presentation.UnitTransformer
import com.sajjad.base.presentation.observe
import com.sajjad.chat.R
import com.sajjad.chat.chat.DaggerChatComponent
import com.sajjad.chat.chat.presentation.adapter.ChatAdapter
import com.sajjad.chat.chat.presentation.adapter.MessageItemDecoration
import com.sajjad.chat.databinding.FragChatBinding
import timber.log.Timber
import javax.inject.Inject

internal class ChatFragment : BaseFragment() {
    @Inject
    lateinit var chatAdapter: ChatAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var fragmentBinding: FragChatBinding

    private val chatViewModel: ChatViewModel
            by viewModels(factoryProducer = { viewModelFactory })
    private val editorViewModel: EditorViewModel
            by viewModels(factoryProducer = { viewModelFactory })

    private val messageText: String
        get() = fragmentBinding.messageEditText.text.toString()

    private val args: ChatFragmentArgs by navArgs()

    private var loading = false
    private var reachedToEnd = false
    private lateinit var rvLinearLayoutManager: LinearLayoutManager

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val lastVisibleItemPosition = rvLinearLayoutManager.findLastVisibleItemPosition()
            if (!loading && !reachedToEnd && (lastVisibleItemPosition + VISIBLE_THRESHOLD) > rvLinearLayoutManager.itemCount) {
                chatViewModel.nextPage(args.contactUsername)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerChatComponent.factory()
            .create(CHAT_PAGE_SIZE)
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.fragmentBinding = FragChatBinding.inflate(inflater)
        return this.fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpChatRecyclerView()
        loadChats()
        observeChatState()
        observeEditorState()
        setSendButtonClickListener()
        setRetryButtonClickListener()
    }

    private fun setSendButtonClickListener() {
        fragmentBinding.sendButton.setOnClickListener {
            editorViewModel.sendMessage(
                messageText,
                args.contactUsername
            )
        }
    }

    private fun setRetryButtonClickListener() {
        fragmentBinding.retryButton.setOnClickListener {
            chatViewModel.reload(args.contactUsername)
        }
    }

    private fun loadChats() {
        chatViewModel.nextPage(args.contactUsername)
    }

    private fun observeChatState() {
        chatViewModel.stateLiveData
            .observe(viewLifecycleOwner) {
                if (it === null) return@observe
                chatLoadingState(it.isLoading)
                chatErrorState(it.isError)
                chatMessagesLoadedState(it.messages)
            }
    }

    private fun chatLoadingState(isLoading: Boolean = true) {
        this.loading = isLoading
        fragmentBinding.run {
            chatProgressBar.isVisible = isLoading
        }
    }

    private fun chatErrorState(isError: Boolean = true) {
        fragmentBinding.run {
            retryButton.isVisible = isError
        }
    }

    private fun chatMessagesLoadedState(messages: List<Message>?) {
        if (messages != null) {
            Timber.d("width of rv: %d", fragmentBinding.chatRecyclerView.width)
            chatAdapter.submitData(messages)
            if (messages.isEmpty()) {
                reachedToEnd
            }
        }
    }

    private fun observeEditorState() {
        editorViewModel.stateLiveData
            .observe(viewLifecycleOwner) {
                if (it === null) return@observe
                messageSendingState(it.sendingMessage)
                messageSendFailed(it.sendingFailed)
                messageSendSuccessful(it.messageSent)
            }
    }

    private fun messageSendingState(isSendingMessage: Boolean = true) {
        fragmentBinding.run {
            sendButton.isVisible = isSendingMessage.not()
            sendProgressBar.isVisible = isSendingMessage
        }
    }

    private fun messageSendFailed(sendingFailed: Boolean = true) {
        if (sendingFailed) {
            Toast.makeText(parentContext, R.string.send_failed, Toast.LENGTH_SHORT).show()
        }
    }

    private fun messageSendSuccessful(message: Message? = null) {
        if (message != null) {
            fragmentBinding.messageEditText.text.clear()
            chatAdapter.submitData(listOf(message))
            fragmentBinding.chatRecyclerView.scrollToPosition(0)
        }
    }

    private fun setUpChatRecyclerView() {
        fragmentBinding.chatRecyclerView.run {
            layoutManager = LinearLayoutManager(
                parentContext, RecyclerView.VERTICAL, true
            ).also {
                this@ChatFragment.rvLinearLayoutManager = it
            }
            adapter = chatAdapter
            addItemDecoration(MessageItemDecoration(UnitTransformer.dp2px(parentContext, 4F)))
            addOnScrollListener(onScrollListener)
        }
    }

    companion object {
        private const val CHAT_PAGE_SIZE = 25
        private const val VISIBLE_THRESHOLD = 3
    }
}