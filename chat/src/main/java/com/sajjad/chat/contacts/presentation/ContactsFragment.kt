package com.sajjad.chat.contacts.presentation

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sajjad.base.dsl.requestPermission
import com.sajjad.base.presentation.BaseFragment
import com.sajjad.base.presentation.observe
import com.sajjad.chat.R
import com.sajjad.chat.contacts.DaggerContactsComponent
import com.sajjad.chat.contacts.domain.model.Contact
import com.sajjad.chat.contacts.presentation.adapter.ContactAdapter
import com.sajjad.chat.contacts.presentation.ui.ContactsPermissionRationalDialog
import com.sajjad.chat.databinding.FragContactsBinding
import javax.inject.Inject

internal class ContactsFragment : BaseFragment() {

    private lateinit var fragmentBinding: FragContactsBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var contactAdapter: ContactAdapter

    private val contactsViewModel: ContactsViewModel
            by viewModels(factoryProducer = { viewModelFactory })

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerContactsComponent.factory()
            .create(
                parentContext.contentResolver
            )
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.fragmentBinding = FragContactsBinding.inflate(inflater)
        return this.fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeContactsState()
        setUpContactsList()
        requestContactReadPermission()
    }

    private fun observeContactsState() {
        contactsViewModel.stateLiveData
            .observe(viewLifecycleOwner) {
                if (it === null) return@observe
                loadingState(it.isLoading)
                errorState(it.isError)

                if (it.contacts != null && it.contacts.isNotEmpty()) {
                    contactsState(it.contacts)
                    emptyState(false)
                } else {
                    emptyState()
                }
            }
    }

    private fun loadingState(isLoading: Boolean = true) {
        if (isLoading) {
            fragmentBinding.progressBar.visibility = View.VISIBLE
        } else {
            fragmentBinding.progressBar.visibility = View.GONE
        }
    }

    private fun errorState(isError: Boolean = true) {
        if (isError) {
            Toast.makeText(parentContext, R.string.error_occurred, Toast.LENGTH_SHORT).show()
        }
    }

    private fun contactsState(contacts: List<Contact>) {
        contactAdapter.contacts = contacts
        contactAdapter.onItemClickListener = { _, contact ->
            contact?.let {
                val action = ContactsFragmentDirections
                    .actionContactsFragmentToChatFragment(it.name)
                findNavController().navigate(action)
            }
        }
    }

    private fun emptyState(isEmpty: Boolean = true) {
        if (isEmpty) {
            fragmentBinding.emptyStateImage.visibility = View.VISIBLE
            fragmentBinding.emptyStateText.visibility = View.VISIBLE
        } else {
            fragmentBinding.emptyStateImage.visibility = View.GONE
            fragmentBinding.emptyStateText.visibility = View.GONE
        }
    }

    private fun setUpContactsList() {
        fragmentBinding.contactsRecyclerView.run {
            layoutManager = LinearLayoutManager(parentContext)
            setHasFixedSize(true)
            adapter = contactAdapter
            addItemDecoration(DividerItemDecoration(parentContext, DividerItemDecoration.VERTICAL))
        }
    }

    private fun showRationalDialog() {
        ContactsPermissionRationalDialog()
            .show(
                childFragmentManager,
                null
            )
    }

    private fun permissionNotGrantedState() {
        Snackbar.make(
            fragmentBinding.root,
            R.string.permission_required,
            Snackbar.LENGTH_INDEFINITE
        ).let { snack ->
            snack.setAction(R.string.ok) {
                snack.dismiss()
            }
            snack.show()
        }
    }

    private fun requestContactReadPermission() {
        requestPermission(
            Manifest.permission.READ_CONTACTS,
            { contactsViewModel.loadContacts() },
            { showRationalDialog() },
            { permissionNotGrantedState() }
        )
    }

}