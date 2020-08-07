package com.sajjad.chat.conversation.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sajjad.chat.conversation.domain.model.Conversation
import javax.inject.Inject

internal class ConversationAdapter @Inject constructor() :
    RecyclerView.Adapter<ConversationViewHolder>() {

    var conversations: List<Conversation>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClickListener: ((position: Int, conversation: Conversation?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder =
        ConversationViewHolder.create(parent, onItemClickListener)

    override fun getItemCount(): Int = conversations?.size ?: 0

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        getItem(position)?.let { conversation ->
            holder.bind(conversation)
        }

    }

    private fun getItem(position: Int): Conversation? = conversations?.get(position)
}