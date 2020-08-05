package com.sajjad.chat.contacts.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.base.beautifyPhoneNumber
import com.sajjad.chat.contacts.domain.model.Contact
import com.sajjad.chat.databinding.ContactRowBinding
import javax.inject.Inject

internal class ContactAdapter @Inject constructor() :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    var contacts: List<Contact>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onItemClickListener: ((position: Int, contact: Contact?) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rowBinding = ContactRowBinding.inflate(inflater)
        return ContactViewHolder(
            rowBinding
        ) { position, contact ->
            onItemClickListener?.let {
                it(position, contact)
            }
        }
    }

    override fun getItemCount(): Int {
        return contacts?.size ?: 0
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    private fun getItem(position: Int): Contact? {
        return contacts?.get(position)
    }

    internal class ContactViewHolder(
        private val binding: ContactRowBinding,
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
    }
}