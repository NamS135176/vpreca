package com.lifecard.vpreca.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.*
import com.lifecard.vpreca.biometric.BioManager
import com.lifecard.vpreca.biometric.BioManagerImpl
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.data.source.SecureStore
import com.lifecard.vpreca.ui.login.LoginResult
import com.lifecard.vpreca.utils.PreferenceHelper
import com.lifecard.vpreca.utils.showInternetTrouble
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.crypto.Cipher
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var userManager: UserManager

    @Inject
    lateinit var secureStore: SecureStore

    private val viewModel: SplashViewModel by viewModels()

    private val bioManager by lazy { createBioManager() }

    private fun createBioManager(): BioManager? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BioManagerImpl(applicationContext)
        } else {
            return null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        viewModel.splashState.observe(this, androidx.lifecycle.Observer { splashResult ->
            splashResult.user?.let { _ ->
                navigateToMainScreen()
            }
            splashResult.error?.let { error ->
//                    error.messageResId?.let { showAlert(getString(it)) }
//                    error.message?.let { showAlert(it) }

                //dont show error here we will navigate to login screen
                userManager.clear()
                navigateToMainScreen()
            }
            splashResult.networkTrouble?.let { networkError ->
                if (networkError) {
                    showInternetTrouble()
                }
            }
        })

        if (userManager.canCallApi && userManager.loginId != null && userManager.memberNumber != null) {

            viewModel.getUser()
        } else {

            if (PreferenceHelper.isEnableBiometricSetting(applicationContext) && bioManager?.checkDeviceSupportBiometric() == true) {
                showBiometricDialog()
            } else {
                navigateToMainScreen()
            }

        }
    }

    private fun showBiometricDialog() {
        val executor = ContextCompat.getMainExecutor(applicationContext)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    navigateToMainScreen()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)

                    result.cryptoObject?.cipher?.let { cipher ->
                        secureStore.updateDecryptBioAuthTokenStore(cipher)
                        secureStore.getAuthToken()?.let { authToken ->
                            userManager.authToken = authToken
                            viewModel.getUser()
                        } ?: kotlin.run { navigateToMainScreen() }

                    } ?: run {
                        navigateToMainScreen()
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
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

    private fun showAlert(content: String) {
        MaterialAlertDialogBuilder(this).apply {
            setPositiveButton(
                R.string.button_ok,
                DialogInterface.OnClickListener { _, _ -> finish() })
            setMessage(content)
        }.create().show()
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    override fun attachBaseContext(newBase: Context?) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                val newOverride = Configuration(newBase?.resources?.configuration)
                newOverride.fontScale = 1.0f
                applyOverrideConfiguration(newOverride)
            }
        } catch (e: Exception) {
        }
        super.attachBaseContext(newBase)
    }
}