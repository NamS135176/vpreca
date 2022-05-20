package com.lifecard.vpreca.ui.fingerprint

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.base.NoToolbarFragment
import com.lifecard.vpreca.biometric.BioManager
import com.lifecard.vpreca.biometric.BioManagerImpl
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.databinding.FragmentFingerprintBinding
import com.lifecard.vpreca.utils.hideLoadingDialog
import com.lifecard.vpreca.utils.showLoadingDialog
import com.lifecard.vpreca.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor

@AndroidEntryPoint
class FingerprintFragment : NoToolbarFragment() {

    companion object {
        fun newInstance() = FingerprintFragment()
    }

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
                showAlert(getString(result.error))
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
                bioManager?.getCryptoObjectForPromtBio()?.let {
                    biometricPrompt.authenticate(promptInfo, it)
                } ?: run {
                    showAlert(getString(R.string.error_fingerprint_not_support))
                }
            } else {
                viewModel.setFingerprintSetting(requireContext(), false)
            }
        }

        if (!viewModel.checkSupportFingerprint(requireContext())) {
            fingerprint.isEnabled = false
            //show alert
            showAlert(getString(R.string.error_fingerprint_not_support))
        }

        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    viewModel.setFingerprintSetting(requireContext(), false)
                    showAlert(errString.toString())
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    bioManager?.getPublicKey()?.let { publicKey: String ->
                        viewModel.uploadPublicKey(
                            publicKey,
                            signature = result.cryptoObject?.signature
                        )
                        viewModel.setFingerprintSetting(requireContext(), true)
                    } ?: run {
                        viewModel.setFingerprintSetting(requireContext(), false)
                        showAlert(getString(R.string.error_bio_authentication_failure))
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    viewModel.setFingerprintSetting(requireContext(), false)
                    showAlert(getString(R.string.error_bio_authentication_failure))
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.prompt_biometric_login_title))
            .setConfirmationRequired(true)
//            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText(getString(R.string.cancel))
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        return binding.root
    }

    fun showAlert(content: String) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setPositiveButton(R.string.button_ok, null)
            setMessage(content)
        }.create().show()
    }
}