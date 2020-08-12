package com.sajjad.auth.profile.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.sajjad.application_component.ApplicationComponentProvider
import com.sajjad.auth.databinding.FragProfileBinding
import com.sajjad.auth.profile.DaggerProfileComponent
import com.sajjad.base.presentation.BaseFragment
import com.sajjad.base.presentation.observe
import com.sajjad.base.session.SessionHolder
import javax.inject.Inject

internal class ProfileFragment : BaseFragment() {

    private lateinit var fragmentBinding: FragProfileBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val profileViewModel: ProfileViewModel
            by viewModels(factoryProducer = { viewModelFactory })

    @Inject
    lateinit var sessionHolder: SessionHolder

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerProfileComponent.factory()
            .create(
                (context.applicationContext as ApplicationComponentProvider)
                    .applicationComponent
                    .provideSessionComponent()
            )
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.fragmentBinding = FragProfileBinding.inflate(inflater)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLogoutButtonClickListener()
        observeSession()
    }

    private fun setLogoutButtonClickListener() {
        profileViewModel.logOut()
    }

    private fun observeSession() {
        this.sessionHolder.sessionLiveData
            .observe(viewLifecycleOwner) {
                if (it == null) return@observe
                setUserName(it.username)
                setPhoneNumber(it.phoneNumber)
            }
    }

    private fun setUserName(username: String?) {
        if (username !== null)
            fragmentBinding.usernameTv.text = username
    }

    private fun setPhoneNumber(phoneNumber: String?) {
        if (phoneNumber != null)
            fragmentBinding.phoneNumberTv.text = phoneNumber
    }

}