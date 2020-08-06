package com.sajjad.chat.chat.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sajjad.base.domain.model.Message
import javax.inject.Inject

internal class ChatAdapter @Inject constructor() : RecyclerView.Adapter<MessageViewHolder>() {

    private var messages: MutableList<Message> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.run {
            bind(getItem(position))
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    private fun getItem(position: Int): Message {
        return messages[position]
    }

    fun submitData(messages: List<Message>) {
        if (this.messages.isEmpty()) {
            this.messages.addAll(messages)
            notifyItemRangeChanged(0, messages.size - 1)
        } else {
            val insertPosition = 0
            this.messages.addAll(insertPosition, messages)
            notifyItemRangeInserted(insertPosition, messages.size)
        }
    }
}