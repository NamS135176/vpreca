package com.lifecard.vpreca.ui.splash

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.*
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.utils.showInternetTrouble
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var userManager: UserManager
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (userManager.canCallApi && userManager.loginId != null && userManager.memberNumber != null) {
            viewModel.splashResult.observe(this, androidx.lifecycle.Observer { splashResult ->
                splashResult.user?.let { _ ->
                    navigateToMainScreen()
                }
                splashResult.error?.let { error ->
                    showAlert(getString(error.messageResId))
                }
                splashResult.networkTrouble?.let { networkError ->
                    if (networkError) {
                        showInternetTrouble()
                    }
                }
            })
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
}