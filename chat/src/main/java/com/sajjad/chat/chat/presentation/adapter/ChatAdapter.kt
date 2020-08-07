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
        val newMessages = messages.filter {
            this.messages.contains(it).not()
        }
        if (newMessages.isEmpty()) return
        val insertPosition = 0
        if (this.messages.isEmpty()) {
            this.messages.addAll(newMessages)
            notifyItemRangeChanged(insertPosition, newMessages.size - 1)
        } else {
            this.messages.addAll(insertPosition, newMessages)
            notifyItemRangeInserted(insertPosition, newMessages.size)
        }
    }
}