package com.lifecard.vpreca.ui.fingerprint

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.biometric.BioManager
import com.lifecard.vpreca.biometric.BioManagerImpl
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.data.source.SecureStore
import com.lifecard.vpreca.databinding.FragmentFingerprintBinding
import com.lifecard.vpreca.utils.hideLoadingDialog
import com.lifecard.vpreca.utils.showAlertMessage
import com.lifecard.vpreca.utils.showLoadingDialog
import com.lifecard.vpreca.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor
import javax.crypto.Cipher
import javax.inject.Inject

@AndroidEntryPoint
class FingerprintFragment : Fragment() {

    companion object {
        fun newInstance() = FingerprintFragment()
    }

    @Inject
    lateinit var userManager: UserManager

    @Inject
    lateinit var secureStore: SecureStore

    private val viewModel: FingerprintViewModel by viewModels()
    private var _binding: FragmentFingerprintBinding? = null
    private val binding get() = _binding!!

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val bioManager by lazy { createBioManager() }

    private fun createBioManager(): BioManager? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BioManagerImpl(requireContext())
        } else {
            return null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.setup(requireContext())

        _binding = FragmentFingerprintBinding.inflate(inflater)
        val buttonBack = binding.appbar.btnBack
        val fingerprint = binding.fingerprint
        buttonBack.setOnClickListener(View.OnClickListener {
            findNavController().popBackStack()
        })
        viewModel.fingerprintSetting.observe(viewLifecycleOwner, Observer {
            fingerprint.isChecked = it
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            when (loading) {
                true -> showLoadingDialog()
                else -> hideLoadingDialog()
            }
        })
        viewModel.registerBiometricResult.observe(viewLifecycleOwner, Observer { result ->
            if (result?.error != null) {
                showAlertMessage(getString(result.error))
            } else if (result?.success != null) {
                showToast(getString(R.string.biometric_setting_success))
            }
            if (result?.bioStatus == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
            ) {
                /*
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG
                    )
                }
                startActivity(enrollIntent)
                 */

            }
        })

        fingerprint.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                bioManager?.getCryptoObjectForPromptBio(Cipher.ENCRYPT_MODE)?.let {
                    biometricPrompt.authenticate(promptInfo, it)
                } ?: run {
                    showAlertMessage(getString(R.string.error_fingerprint_not_support))
                }
            } else {
                viewModel.setFingerprintSetting(requireContext(), false)
                userManager.authToken?.let { authToken ->
                    secureStore.moveAuthTokenToNormalStore(
                        authToken
                    )
                }
            }
        }

        if (!viewModel.checkSupportFingerprint(requireContext())) {
            fingerprint.isEnabled = false
            viewModel.setFingerprintSetting(requireContext(), false)
        }

        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    viewModel.handleAuthenticationError(errorCode, errString)
                    viewModel.setFingerprintSetting(requireContext(), false)
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    result.cryptoObject?.cipher?.let {
                        secureStore.updateEncryptBioAuthTokenStore(it)
                        userManager.authToken?.let { authToken ->
                            secureStore.saveAuthToken(
                                authToken
                            )
                        }
                        viewModel.setFingerprintSetting(requireContext(), true)
                        //show toast
                        showToast(getString(R.string.biometric_setting_success))
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    viewModel.setFingerprintSetting(requireContext(), false)
//                    showAlert(getString(R.string.error_bio_authentication_failure))
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.prompt_biometric_login_title))
            .setConfirmationRequired(true)
            .setNegativeButtonText(getString(R.string.cancel))
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        return binding.root
    }
}