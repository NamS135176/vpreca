package com.lifecard.vpreca.ui.fingerprint

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.base.NoToolbarFragment
import com.lifecard.vpreca.databinding.FragmentFingerprintBinding
import java.util.concurrent.Executor

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFingerprintBinding.inflate(inflater)
        val buttonBack = binding.appbar.btnBack
        val fingerprint = binding.fingerprint
        buttonBack.setOnClickListener(View.OnClickListener {
            findNavController().popBackStack()
        })
        viewModel.fingerprintSetting.observe(viewLifecycleOwner, Observer {
            fingerprint.isChecked = it
        })

        fingerprint.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                viewModel.setFingerprintSetting(false)
            }
        }
        if (!viewModel.checkSupportFingerprint()) {
            fingerprint.isEnabled = false
            //show alert
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton(R.string.button_ok, DialogInterface.OnClickListener { _, _ ->
                    findNavController().popBackStack()
                })
                setMessage(getString(R.string.error_fingerprint_not_support))
            }.create().show()
        }

        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    viewModel.setFingerprintSetting(false)
                    Toast.makeText(
                        context,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    viewModel.setFingerprintSetting(true)
                    Toast.makeText(
                        context,
                        "Authentication succeeded!", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    viewModel.setFingerprintSetting(false)
                    Toast.makeText(
                        context, "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.prompt_biometric_login_title))
//            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText(getString(R.string.cancel))
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        return binding.root
    }

}