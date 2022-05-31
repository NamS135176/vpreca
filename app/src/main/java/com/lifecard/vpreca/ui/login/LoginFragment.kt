package com.lifecard.vpreca.ui.login

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.base.NoToolbarFragment
import com.lifecard.vpreca.biometric.BioManager
import com.lifecard.vpreca.biometric.BioManagerImpl
import com.lifecard.vpreca.biometric.BiometricType
import com.lifecard.vpreca.data.source.SecureStore
import com.lifecard.vpreca.databinding.FragmentLoginBinding
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : NoToolbarFragment() {
    @Inject
    lateinit var secureStore: SecureStore
    private val loginViewModel: LoginViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var passwordEditText: AppCompatEditText
    private lateinit var lifecycleObserver: DefaultLifecycleObserver

    private val bioManager by lazy { createBioManager() }

    private fun createBioManager(): BioManager? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BioManagerImpl(requireContext())
        } else {
            return null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val usernameLayout = binding.usernameLayout
        val usernameEditText = binding.username
        val passwordLayout = binding.passwordLayout
        passwordEditText = binding.password
        val loginButton = binding.buttonLogin
        val buttonBioLogin = binding.buttonBioLogin
        val loadingProgressBar = binding.loading
        val logoGift = binding.logoGift
        val signUpButton = binding.buttonSignup
        val btnForgotPass = binding.buttonForgotPassword
        val tvLoginInfo = binding.textLoginInfo

        tvLoginInfo.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_sms_verify) })

        btnForgotPass.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_forgot_input)
        })

        signUpButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_policy)
        })

        loginViewModel.validForm.observe(viewLifecycleOwner, Observer { loginFormState ->
            loginButton.isEnabled =
                loginFormState.usernameError == null && loginFormState.passwordError == null
            println("loginViewModel.validForm.observe... usernameError: ${loginFormState.usernameError} - passwordError: ${loginFormState.passwordError} ")
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
                loginResult.errorText?.let { errorText ->
                    showAlert(errorText)
                }
                loginResult.success?.let {
                    navigateToHome()
                }
            })

        loginViewModel.loading.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    loadingProgressBar.visibility = View.VISIBLE
                    loginButton.visibility = View.INVISIBLE
                    buttonBioLogin.isEnabled = false
                }
                else -> {
                    loadingProgressBar.visibility = View.GONE
                    loginButton.visibility = View.VISIBLE
                    buttonBioLogin.isEnabled = true
                }
            }
        })

        val savedUsername = secureStore.getLoginUserId()
        savedUsername?.let {
            usernameEditText.text = it.toEditable()
        }
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

        when (bioManager?.checkDeviceSupportBiometric() == true || PreferenceHelper.isEnableBiometricSetting(
            requireContext()
        )) {
            true -> buttonBioLogin.visibility = View.VISIBLE
            else -> buttonBioLogin.visibility = View.GONE
        }
        when (bioManager?.getBiometricTypeForAuthentication()) {
            BiometricType.FACE -> {
                buttonBioLogin.icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_android_faceid)
                buttonBioLogin.text = getString(R.string.action_sign_in_faceid)
            }
            BiometricType.FINGERPRINT -> {
                buttonBioLogin.icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_android_fingerprint)
                buttonBioLogin.text = getString(R.string.action_sign_in_fingerprint)
            }
            else -> buttonBioLogin.visibility = View.GONE
        }

        buttonBioLogin.setOnClickListener(View.OnClickListener {
            showBiometricDialog()
        })

        logoGift.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_introduce_first)
        })
        return binding.root

    }

    private fun showBiometricDialog() {
        if (!loginViewModel.checkUsername(binding.username.text.toString())) {
            return
        }
        val executor = ContextCompat.getMainExecutor(requireContext())
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    loginViewModel.handleAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    result.cryptoObject?.signature?.let { signature ->
                        loginViewModel.loginWithBio(
                            binding.username.text.toString(),
                            signature = signature
                        )
                    } ?: run {
                        showAlert(getString(R.string.error_bio_authentication_failure))
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
//                    showAlert(getString(R.string.error_bio_authentication_failure))
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.prompt_biometric_login_title))
            .setConfirmationRequired(true)
//            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText(getString(R.string.cancel))
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        bioManager?.getCryptoObjectForPromtBio()?.let {
            biometricPrompt.authenticate(promptInfo, it)
        } ?: run {
            showAlert(getString(R.string.error_bio_authentication_failure))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycleObserver = object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)

                passwordEditText.text = "".toEditable()
            }
        }
        requireActivity().lifecycle.addObserver(lifecycleObserver)
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().lifecycle.removeObserver(lifecycleObserver)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        showAlert(getString(errorString))
    }

    fun showAlert(content: String) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setPositiveButton(R.string.button_ok, null)
            setMessage(content)
        }.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}