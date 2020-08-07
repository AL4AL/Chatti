package com.sajjad.chat.chat.presentation.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseUser
import com.sajjad.base.domain.model.Message
import com.sajjad.chat.R
import com.sajjad.chat.databinding.MessageItemBinding

internal class MessageViewHolder(
    private val itemBinding: MessageItemBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(message: Message) {
        itemBinding.message = message
        if (message.from == ParseUser.getCurrentUser().username) {
            alignRight()
            greenizeIt()
        }
    }

    private fun alignRight() {
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        itemBinding.root.layoutParams = params
        val cardAttr = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        cardAttr.gravity = Gravity.RIGHT
        itemBinding.card.layoutParams = cardAttr
    }

    private fun greenizeIt() {
        itemBinding.run {
            card.setCardBackgroundColor(
                root.context.getColor(R.color.self_message_background)
            )
            timeTv.setTextColor(
                root.context.getColor(R.color.self_message_time_text_color)
            )
        }

    }

    companion object {
        fun create(parent: ViewGroup): MessageViewHolder {
            return MessageViewHolder(
                MessageItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    )
                )
            )
        }
    }

}