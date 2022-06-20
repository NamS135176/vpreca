package com.lifecard.vpreca.ui.login

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
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
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.data.model.LoginIdData
import com.lifecard.vpreca.data.source.SecureStore
import com.lifecard.vpreca.databinding.FragmentLoginBinding
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.crypto.Cipher
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : NoToolbarFragment() {
    @Inject
    lateinit var secureStore: SecureStore

    @Inject
    lateinit var userManager: UserManager

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
    ): View {

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
        val buttonLoginLandlinePhone = binding.buttonLoginLandlinePhone

        buttonLoginLandlinePhone.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton(R.string.button_ok) { _, _ ->
                    openBrowser("https://vpcevssl.lifecard.co.jp/LW01/LW0102OP01BL.do")
                }
                    .setNegativeButton(R.string.button_cancel, null)
                setMessage(getString(R.string.alert_landline_phone))
            }.create().show()
        }


        btnForgotPass.setOnClickListener {
            findNavController().navigate(R.id.nav_forgot_input)
        }

        signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_to_policy)
        }

        loginViewModel.validForm.observe(viewLifecycleOwner) { isValid ->
            loginButton.isEnabled = isValid
            loginButton.alpha = if (isValid) 1.0f else 0.65f
        }
        loginViewModel.usernameError.observe(viewLifecycleOwner) { error: Int? ->
            usernameLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }

        }
        loginViewModel.passwordError.observe(viewLifecycleOwner) { error: Int? ->
            passwordLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer

                loginResult.networkTrouble?.let { if (it) showInternetTrouble() }
                loginResult.smsVerification?.let {
                    if (it) {
                        val action = LoginFragmentDirections.actionToSms(LoginIdData(usernameEditText.text.toString()))
                        findNavController().navigate(action)
                    }
                }
                loginResult.error?.errorMessage?.let { showLoginFailed(it) }
                loginResult.errorText?.let { errorText ->
                    showAlert(errorText)
                }
                loginResult.success?.let {
                    navigateToHome()
                }
            })

        loginViewModel.loading.observe(viewLifecycleOwner) {
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
        }

        usernameEditText.doAfterTextChanged {
            loginViewModel.checkValidForm(
                username = usernameEditText.text.toString(),
                password = passwordEditText.text.toString()
            )
        }
        usernameEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //focus to password field
                passwordEditText.requestFocus()
            }
            false
        }

        passwordEditText.doAfterTextChanged {
            loginViewModel.checkValidForm(
                username = usernameEditText.text.toString(),
                password = passwordEditText.text.toString()
            )
        }
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

        when (bioManager?.checkDeviceSupportBiometric() == true && PreferenceHelper.isEnableBiometricSetting(
            requireContext()
        )) {
            true -> buttonBioLogin.visibility = View.VISIBLE
            else -> buttonBioLogin.visibility = View.GONE
        }
        buttonBioLogin.setOnClickListener {
            showBiometricDialog()
        }

        logoGift.setOnClickListener {
            findNavController().navigate(R.id.nav_introduce_first)
        }

//        if (PreferenceHelper.isEnableBiometricSetting(requireContext()) && bioManager?.checkDeviceSupportBiometric() == true) {
//            showBiometricDialog()
//        }
        return binding.root

    }

    private fun showBiometricDialog() {
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

                    result.cryptoObject?.cipher?.let { cipher ->
                        loginViewModel.loginWithCipher(cipher = cipher)

                    } ?: run {
                        showAlert(getString(R.string.error_bio_authentication_failure))
                    }
                }

            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.prompt_biometric_login_title))
            .setConfirmationRequired(true)
            .setNegativeButtonText(getString(R.string.cancel))
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        bioManager?.getCryptoObjectForPromptBio(Cipher.DECRYPT_MODE)?.let {
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

            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                if (PreferenceHelper.isEnableBiometricSetting(requireContext()) && bioManager?.checkDeviceSupportBiometric() == true) {
                    showBiometricDialog()
                }
            }
        }
        requireActivity().lifecycle.addObserver(lifecycleObserver)
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().lifecycle.removeObserver(lifecycleObserver)
    }

    private fun showLoginFailed(errorMessage: String) {
//        showAlert(errorMessage)
        showPopupMessage(message = errorMessage)
    }

    fun showAlert(content: String) {
        showPopupMessage(message = content)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        loginViewModel.clearLoginResultError()
    }
}