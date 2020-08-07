package com.sajjad.chat.conversation.presentation.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.sajjad.chat.conversation.domain.model.Conversation
import com.sajjad.chat.databinding.ConversationItemBinding

internal class ConversationViewHolder(
    private val itemBinding: ConversationItemBinding,
    private inline val onItemClickListener: ((position: Int, conversation: Conversation?) -> Unit)? = null
) : RecyclerView.ViewHolder(itemBinding.root) {

    private var conversation: Conversation? = null

    init {
        itemBinding.root.setOnClickListener {
            onItemClickListener?.let { listener ->
                listener(
                    adapterPosition,
                    conversation
                )
            }
        }
    }

    fun bind(conversation: Conversation) {
        this.conversation = conversation
        itemBinding.conversation = conversation
        itemBinding.avatarIv.setImageDrawable(getTextDrawable(conversation.username.take(1)))
    }

    private fun getTextDrawable(char: String): Drawable {
        val color = ColorGenerator.MATERIAL.getColor(char.hashCode())
        return TextDrawable.builder()
            .buildRound(char.toUpperCase(), color)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClickListener: ((position: Int, conversation: Conversation?) -> Unit)? = null
        ) = ConversationViewHolder(
            ConversationItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            ),
            onItemClickListener
        )
    }
}