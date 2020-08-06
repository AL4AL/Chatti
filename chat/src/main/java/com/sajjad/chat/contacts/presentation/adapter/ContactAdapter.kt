package com.sajjad.chat.contacts.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sajjad.chat.contacts.domain.model.Contact
import javax.inject.Inject

internal class ContactAdapter @Inject constructor() :
    RecyclerView.Adapter<ContactViewHolder>() {

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
        return ContactViewHolder.create(parent, onItemClickListener)
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
}