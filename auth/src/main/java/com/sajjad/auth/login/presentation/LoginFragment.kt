package com.sajjad.auth.login.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.sajjad.auth.databinding.FragLoginBinding
import com.sajjad.auth.login.DaggerLoginComponent
import com.sajjad.base.presentation.BaseFragment
import javax.inject.Inject

internal class LoginFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val loginViewModel: LoginViewModel
            by viewModels(factoryProducer = { viewModelFactory })

    private lateinit var fragmentBinding: FragLoginBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerLoginComponent.factory()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.fragmentBinding = FragLoginBinding.inflate(inflater)
        return this.fragmentBinding.root
    }
}