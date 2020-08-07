package com.sajjad.chat.contacts.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.base.beautifyPhoneNumber
import com.sajjad.chat.contacts.domain.model.Contact
import com.sajjad.chat.databinding.ContactItemBinding

internal class ContactViewHolder(
    private val binding: ContactItemBinding,
    private inline val onItemClickListener: ((position: Int, contact: Contact?) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {
    private var contact: Contact? = null

    init {
        binding.root.setOnClickListener {
            onItemClickListener?.let { listener ->
                listener(
                    adapterPosition,
                    contact
                )
            }
        }
    }

    fun bind(contact: Contact) {
        this.contact = contact.also {
            it.phoneNumber = it.phoneNumber.beautifyPhoneNumber()
        }
        binding.contact = contact
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onItemClickListener: ((position: Int, contact: Contact?) -> Unit)? = null
        ): ContactViewHolder {

            val view = ContactItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            ).also {
                it.root.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            return ContactViewHolder(
                view,
                onItemClickListener
            )
        }
    }
}