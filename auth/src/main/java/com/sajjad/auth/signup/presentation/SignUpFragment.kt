package com.sajjad.auth.signup.presentation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sajjad.auth.R
import com.sajjad.auth.databinding.FragSignupBinding
import com.sajjad.auth.signup.DaggerSignUpComponent
import com.sajjad.base.presentation.BaseFragment
import com.sajjad.base.presentation.observe
import javax.inject.Inject

class SignUpFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var fragmentBinding: FragSignupBinding
    private val signUpViewModel: SignUpViewModel
            by viewModels(factoryProducer = { viewModelFactory })

    private val username: String
        get() = fragmentBinding.signUpUsernameInputLayout.editText!!.text.toString()
    private val password: String
        get() = fragmentBinding.signUpPasswordInputLayout.editText!!.text.toString()
    private val phoneNumber: String
        get() = fragmentBinding.signUpPhoneInputLayout.editText!!.text.toString()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerSignUpComponent.factory()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.fragmentBinding = FragSignupBinding.inflate(inflater)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSignUpState()
        setSignUpButtonClickListener()
        setLoginTvClickListener()
    }

    private fun observeSignUpState() {
        signUpViewModel.stateLiveData
            .observe(viewLifecycleOwner) {
                if (it == null) return@observe
                errorState(it.isError)
                loadingState(it.isLoading)
                userNameAlreadyExistsState(it.isUsernameTaken)
                phoneNumberAlreadyExistsState(it.phoneNumberTaken)
                userSignedUpState(it.isSuccessful)
            }
    }

    private fun errorState(isError: Boolean = true) {
        if (isError) {
            Toast.makeText(parentContext, R.string.error_try_again, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadingState(isLoading: Boolean = true) {
        fragmentBinding.run {
            signUpButton.visibility = if (isLoading) View.INVISIBLE else {
                View.VISIBLE
            }
            progressBar.isVisible = isLoading
            signUpUsernameInputLayout.editText!!.isEnabled = isLoading.not()
            signUpPasswordInputLayout.editText!!.isEnabled = isLoading.not()
            signUpPhoneInputLayout.editText!!.isEnabled = isLoading.not()
        }
    }

    private fun userNameAlreadyExistsState(alreadyExists: Boolean = true) {
        if (alreadyExists) {
            Toast.makeText(parentContext, R.string.username_already_exists, Toast.LENGTH_SHORT)
                .show()
            fragmentBinding.signUpUsernameInputLayout.error =
                getString(R.string.username_already_exists)
        }
    }

    private fun phoneNumberAlreadyExistsState(exists: Boolean = true) {
        if (exists) {
            Toast.makeText(parentContext, R.string.phone_number_already_exists, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun userSignedUpState(isSuccessful: Boolean = true) {
        if (isSuccessful) {
            findNavController().popBackStack()
            findNavController().popBackStack()
            findNavController().navigate(Uri.parse(getString(R.string.conversations_deep_link)))
        }
    }

    private fun setSignUpButtonClickListener() {
        fragmentBinding.signUpButton.setOnClickListener {
            if (validateFields()) {
                signUpViewModel.signUp(
                    username,
                    password,
                    phoneNumber
                )
            }
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true
        if (username.isBlank()) {
            fragmentBinding.signUpUsernameInputLayout.error = getString(R.string.username_is_empty)
            isValid = false
        } else {
            fragmentBinding.signUpUsernameInputLayout.error = null
        }
        if (password.isBlank()) {
            fragmentBinding.signUpPasswordInputLayout.error = getString(R.string.password_is_empty)
            isValid = false
        } else {
            fragmentBinding.signUpPasswordInputLayout.error = null
        }
        if (phoneNumber.isBlank()) {
            fragmentBinding.signUpPhoneInputLayout.error = getString(R.string.phone_number_is_empty)
            isValid = false
        } else {
            fragmentBinding.signUpPhoneInputLayout.error = null
        }
        return isValid
    }

    private fun setLoginTvClickListener() {
        fragmentBinding.login.setOnClickListener {
            val action = SignUpFragmentDirections.actionSignUpFormFragmentPop()
            findNavController().navigate(action)
        }
    }

}