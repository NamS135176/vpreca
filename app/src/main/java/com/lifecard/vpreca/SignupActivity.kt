package com.lifecard.vpreca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

class SignupActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val navHostFragment  = supportFragmentManager.findFragmentById(
            R.id.fmSignup
        ) as NavHostFragment
        navController = navHostFragment.navController
    }
}