package com.lifecard.vpreca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lifecard.vpreca.ui.termofuse.TermOfUseFragment

class TermOfUseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.term_of_use_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TermOfUseFragment.newInstance())
                .commitNow()
        }
    }
}