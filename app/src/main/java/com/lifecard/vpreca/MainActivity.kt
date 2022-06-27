package com.lifecard.vpreca

import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.databinding.ActivityMainBinding
import com.lifecard.vpreca.eventbus.CloseDrawerEvent
import com.lifecard.vpreca.ui.custom.DrawerMenuLayout
import com.lifecard.vpreca.utils.KeyboardUtils
import com.lifecard.vpreca.utils.PreferenceHelper
import com.lifecard.vpreca.utils.lockDrawer
import com.scottyab.rootbeer.RootBeer
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
    lateinit var userManager: UserManager

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: DrawerMenuLayout

    var currentToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (!BuildConfig.FLAVOR.contentEquals("QA", ignoreCase = true)) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }

        val rootBeer = RootBeer(this)
        val isRooted = rootBeer.isRooted
        if (isRooted) {
            //we found indication of root
            //show alert root
            MaterialAlertDialogBuilder(this).apply {
                setPositiveButton(
                    R.string.button_ok, DialogInterface.OnClickListener { _, _ -> finish() }
                )
                setMessage(getString(R.string.root_error))
            }.create().show()
        } else {
            //we didn't find indication of root
            bindingView()
        }
    }

    private fun bindingView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.appBarMain.toolbar
        setSupportActionBar(binding.appBarMain.toolbar)

        drawerLayout = binding.drawerLayout
        navView = binding.navView


        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val forceShowLogin = intent.getBooleanExtra(EXTRA_FORCE_SHOW_LOGIN, false)
        if (forceShowLogin) userManager.clear()

        if (userManager.isLoggedIn) {
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
            //hide keyboard due issue https://bjm.backlog.com/view/LC_VPC-166 on Aquos SHV45, GalaxyS4
            KeyboardUtils.hideKeyboard(this, binding.root)
            if (destination.id == R.id.nav_home) {
                toolbar.visibility = View.VISIBLE
            } else {
                toolbar.visibility = View.GONE
            }
            //debug backstack
            if (BuildConfig.DEBUG) {
                println("addOnDestinationChangedListener ... backQueue count = ${navController.backQueue.count()}")
                for (i in 0 until navController.backQueue.count()) {
                    val destination = navController.backQueue[i].destination
                    println("addOnDestinationChangedListener ...stack[$i]= (id = ${destination.id}, label = ${destination.label}, displayName = ${destination.displayName}, navigatorName = ${destination.navigatorName}, destination = $destination)")
                }
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

    override fun attachBaseContext(newBase: Context?) {
        newBase?.let { context ->
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    val newOverride = Configuration(context.resources.configuration)
                    newOverride.fontScale = 1.0f

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        val displayMetrics = context.resources?.displayMetrics
                        if (displayMetrics?.densityDpi != DisplayMetrics.DENSITY_DEVICE_STABLE) {
                            newOverride.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE
                        }
                    }
                    applyOverrideConfiguration(newOverride)
                }
            } catch (e: Exception) {
                println(e)
            }
        }

        super.attachBaseContext(newBase)
    }

    override fun onDestroy() {
        super.onDestroy()
        currentToast?.cancel()
    }
}