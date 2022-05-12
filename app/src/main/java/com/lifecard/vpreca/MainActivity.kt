package com.lifecard.vpreca

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.lifecard.vpreca.databinding.ActivityMainBinding
import com.lifecard.vpreca.eventbus.CloseDrawerEvent
import com.lifecard.vpreca.ui.custom.DrawerMenuLayout
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: DrawerMenuLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        drawerLayout = binding.drawerLayout
        navView = binding.navView


        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

//        val toggle = ActionBarDrawerToggle(
//            this,
//            drawerLayout,
//            binding.appBarMain.toolbar,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        )
//        toggle.isDrawerIndicatorEnabled = true
//        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer_toggle);
//        actionBar?.setDisplayHomeAsUpEnabled(true)
//        actionBar?.setHomeButtonEnabled(true)

//        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorAccent));

//        toggle.drawerArrowDrawable.color = ContextCompat.getColor(applicationContext, R.color.black)

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
}