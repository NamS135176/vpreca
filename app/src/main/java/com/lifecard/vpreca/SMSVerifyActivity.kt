package com.lifecard.vpreca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

class SMSVerifyActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smsverify)
        val navHostFragment  = supportFragmentManager.findFragmentById(
            R.id.fmSMS
        ) as NavHostFragment
        navController = navHostFragment.navController
    }
}