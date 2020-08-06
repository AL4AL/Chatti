package com.sajjad.auth.login.presentation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sajjad.auth.R
import com.sajjad.auth.databinding.FragLoginBinding
import com.sajjad.auth.login.DaggerLoginComponent
import com.sajjad.base.presentation.BaseFragment
import com.sajjad.base.presentation.observe
import javax.inject.Inject

internal class LoginFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val loginViewModel: LoginViewModel
            by viewModels(factoryProducer = { viewModelFactory })

    private val username: String
        get() = fragmentBinding.loginUsernameInputLayout.editText!!.text.toString()
    private val password: String
        get() = fragmentBinding.loginPasswordInputLayout.editText!!.text.toString()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLoginButtonClickListener()
        setSignUpTvClickListener()
        observeLoginState()
    }


    private fun setLoginButtonClickListener() {
        fragmentBinding.loginButton.setOnClickListener {
            if (validateFields()) {
                loginViewModel.login(
                    username,
                    password
                )
            }
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true
        if (username.isBlank()) {
            fragmentBinding.loginUsernameInputLayout.error = getString(R.string.username_is_empty)
            isValid = false
        } else {
            fragmentBinding.loginUsernameInputLayout.error = null
        }
        if (password.isBlank()) {
            fragmentBinding.loginPasswordInputLayout.error = getString(R.string.password_is_empty)
            isValid = false
        } else {
            fragmentBinding.loginPasswordInputLayout.error = null
        }
        return isValid
    }

    private fun setSignUpTvClickListener() {
        fragmentBinding.signUp.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
    }

    private fun observeLoginState() {
        loginViewModel.stateLiveData
            .observe(viewLifecycleOwner) { state ->
                if (state === null) return@observe
                loadingState(state.isLoading)
                successState(state.isSuccessful)
                errorState(state.isError)
                invalidUserPassState(state.invalidUserPass)
            }
    }

    private fun loadingState(isLoading: Boolean = true) {
        if (isLoading) {
            fragmentBinding.loginButton.visibility = View.INVISIBLE
            fragmentBinding.progressBar.visibility = View.VISIBLE
        } else {
            fragmentBinding.loginButton.visibility = View.VISIBLE
            fragmentBinding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun successState(isSuccessful: Boolean = true) {
        if (isSuccessful) {
            findNavController().popBackStack()
            findNavController().navigate(Uri.parse(getString(R.string.conversations_deep_link)))
        }
    }

    private fun errorState(isError: Boolean = true) {
        if (isError) {
            Toast.makeText(parentContext, getString(R.string.error_try_again), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun invalidUserPassState(isInvalid: Boolean = true) {
        if (isInvalid) {
            Toast.makeText(parentContext, R.string.invalid_user_name_password, Toast.LENGTH_SHORT)
                .show()
        }
    }
}