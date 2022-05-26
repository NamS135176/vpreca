package com.lifecard.vpreca

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.databinding.ActivityMainBinding
import com.lifecard.vpreca.eventbus.CloseDrawerEvent
import com.lifecard.vpreca.ui.custom.DrawerMenuLayout
import com.lifecard.vpreca.ui.login.LoginFragmentDirections
import com.lifecard.vpreca.utils.PreferenceHelper
import com.lifecard.vpreca.utils.lockDrawer
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        var EXTRA_FORCE_SHOW_LOGIN = "force_show_login"
        fun createBundle(forceShowLogin: Boolean = false): Bundle {
            return bundleOf(EXTRA_FORCE_SHOW_LOGIN to forceShowLogin)
        }
    }

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: DrawerMenuLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.appBarMain.toolbar
        setSupportActionBar(binding.appBarMain.toolbar)

        drawerLayout = binding.drawerLayout
        navView = binding.navView


        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val forceShowLogin = intent.getBooleanExtra(EXTRA_FORCE_SHOW_LOGIN, false)
        if (forceShowLogin) userRepository.clear()

        if (userRepository.isLoggedIn) {
            navController.setGraph(R.navigation.main_navigation)
        } else {
            lockDrawer()
            if (!PreferenceHelper.isAcceptTermOfUseFirstTime(appContext = baseContext)) {
                val navOptions = NavOptions.Builder().apply {
                    setPopUpTo(R.id.nav_term_of_use, inclusive = true)
                }
                    .build()
                navController.navigate(R.id.nav_term_of_use, args = null, navOptions = navOptions)
            }
        }

        navController.enableOnBackPressed(true)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.nav_home) {
                toolbar.visibility = View.VISIBLE
            } else {
                toolbar.visibility = View.GONE
            }
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun handleCloseDrawer(event: CloseDrawerEvent) {
        drawerLayout.close()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}