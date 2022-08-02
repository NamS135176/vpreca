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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.MainActivity
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.data.source.SecureStore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var userManager: UserManager

    @Inject
    lateinit var secureStore: SecureStore

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        viewModel.splashState.observe(this, androidx.lifecycle.Observer { splashResult ->
            splashResult.user?.let { _ ->
                navigateToMainScreen()
            }
            splashResult.error?.let { error ->

                //dont show error here we will navigate to login screen
                userManager.clear()
                navigateToMainScreen()
            }
            splashResult.networkTrouble?.let { networkError ->
                if (networkError) {
                    navigateToMainScreen()
                }
            }
        })

        if (userManager.canCallApi && userManager.loginId != null && userManager.memberNumber != null) {
            viewModel.getUser()
        } else {
            navigateToMainScreen()
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