package com.lifecard.vpreca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.lifecard.vpreca.ui.introduce.IntroduceFragmentFirst

class IntroduceActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.introduce_activity)
        val navHostFragment  = supportFragmentManager.findFragmentById(
            R.id.fmIntroduce
        ) as NavHostFragment
        navController = navHostFragment.navController
    }
}