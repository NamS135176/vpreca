package com.lifecard.vpreca.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.lifecard.vpreca.*
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.utils.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (PreferenceHelper.isAcceptTermOfUseFirstTime(appContext = baseContext)) {
            navigateToTermOfUse()
        } else {
            navigateToLoginScreen()
//            if (userRepository.isLoggedIn) {
//                navigateToLoginScreen()
//            } else {
//                navigateToMainScreen()
//            }
        }
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun navigateToSignUpScreen() {
        val intent = Intent(this, SignupActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun navigateToTermOfUse() {
        val intent = Intent(this, TermOfUseActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}