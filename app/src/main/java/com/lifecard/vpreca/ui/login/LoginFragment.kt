package com.lifecard.vpreca.ui.login

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.IntroduceActivity
import com.lifecard.vpreca.MainActivity

import com.lifecard.vpreca.R
import com.lifecard.vpreca.SignupActivity
import com.lifecard.vpreca.data.model.User
import com.lifecard.vpreca.databinding.FragmentLoginBinding
import com.lifecard.vpreca.utils.KeyboardUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameLayout = binding.usernameLayout
        val usernameEditText = binding.username
        val passwordLayout = binding.passwordLayout
        val passwordEditText = binding.password
        val loginButton = binding.buttonLogin
        val loadingProgressBar = binding.loading
        val logoGift = binding.logoGift
        val signUpButton = binding.buttonSignup

        signUpButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(requireContext(), SignupActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
//            Toast.makeText(requireContext(),"sdhgfjsf", Toast.LENGTH_SHORT)
        })

        loginViewModel.validForm.observe(viewLifecycleOwner, Observer { loginFormState ->
            loginButton.isEnabled =
                loginFormState.usernameError == null && loginFormState.passwordError == null
            print("loginViewModel.validForm.observe... usernameError: ${loginFormState.usernameError} - passwordError: ${loginFormState.passwordError} ")
        })
        loginViewModel.usernameError.observe(viewLifecycleOwner, Observer { error: Int? ->
            usernameLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }

        })
        loginViewModel.passwordError.observe(viewLifecycleOwner, Observer { error: Int? ->
            passwordLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer

                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                    navigateToMainScreen()
                }
            })

        loginViewModel.loading.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    loadingProgressBar.visibility = View.VISIBLE
                    loginButton.visibility = View.INVISIBLE
                }
                else -> {
                    loadingProgressBar.visibility = View.GONE
                    loginButton.visibility = View.VISIBLE
                }
            }
        })

        usernameEditText.doAfterTextChanged { text -> loginViewModel.usernameDataChanged(text = text.toString()) }
        usernameEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //focus to password field
                passwordEditText.requestFocus()
            }
            false
        }

        passwordEditText.doAfterTextChanged { text -> loginViewModel.passwordDataChanged(text = text.toString()) }
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
                context?.let { KeyboardUtils.hideKeyboard(it, passwordEditText) }
            }
            false
        }

        loginButton.setOnClickListener {
            loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
            context?.let { KeyboardUtils.hideKeyboard(it, passwordEditText) }
        }

        logoGift.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, IntroduceActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        })
    }

    private fun updateUiWithUser(user: User) {
        val welcome = getString(R.string.welcome) + user.email
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setPositiveButton(R.string.button_ok, null)
            setMessage(errorString)
        }.create().show()
    }


    private fun navigateToMainScreen() {
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}