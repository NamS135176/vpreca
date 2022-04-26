package com.lifecard.vpreca.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import com.lifecard.vpreca.*
import com.lifecard.vpreca.data.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)
//        val sharedPref = this?.getPreferences(MODE_PRIVATE) ?: return
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val first = sharedPref.getString("first", "no")
        Toast.makeText(this,first, Toast.LENGTH_SHORT).show()
        if(first == "no"){
            navigateToTermOfUse()
        }
        else{
            navigateToLoginScreen()
        }
//        val editor = sharedPref.edit()
//        editor.putString("nameFirst_key", namefirst)
//        editor.apply()
//        editor.commit()

//        println("SplashActivity... onCreate: ${userRepository.user}")
//        if (userRepository.isLoggedIn) {
//            navigateToLoginScreen()
//        } else {
//            navigateToMainScreen()
//        }
//        navigateToLoginScreen()
//        navigateToSignUpScreen()

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