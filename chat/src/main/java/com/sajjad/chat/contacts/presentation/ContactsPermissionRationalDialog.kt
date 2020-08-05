package com.sajjad.chat.contacts.presentation.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.sajjad.chat.R

internal class ContactsPermissionRationalDialog : DialogFragment() {

    private lateinit var parentContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.parentContext = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(parentContext)
            .setTitle(R.string.permission_required)
            .setMessage(R.string.we_need_you_to_grant_contacts_permission_to_load_them)
            .setPositiveButton(R.string.ok) { _, _ -> dismiss() }
            .create()
    }
}