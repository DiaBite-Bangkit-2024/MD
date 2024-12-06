package com.capstone.diabite.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.capstone.diabite.R
import com.capstone.diabite.ui.settings.SettingsPreferences
import com.capstone.diabite.databinding.ActivityMainBinding
import com.capstone.diabite.ui.dashboard.DashboardViewModel
import com.capstone.diabite.ui.login.LoginViewModel
import com.capstone.diabite.ui.settings.SettingsViewModel
import com.capstone.diabite.ui.settings.SettingsViewModelFactory
import com.capstone.diabite.ui.settings.dataStore
import com.capstone.diabite.view.auth.AuthViewModelFactory
import me.ibrahimsn.lib.SmoothBottomBar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(this)
    }
    private lateinit var navController: NavController
    private lateinit var navView: SmoothBottomBar
    private var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val pref = SettingsPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingsViewModelFactory(pref))[SettingsViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive == true) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView
        navView.setBackgroundColor(android.graphics.Color.TRANSPARENT)

        navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard, R.id.navigation_articles, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bottom_nav_menu)
        val menu = popupMenu.menu
        navView.setupWithNavController(menu, navController)
//        setupSmoothBottomMenu()

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, OnBoardingActivity::class.java))
                finish()
            }
        }


        val sharedViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        sharedViewModel.name.value = getExtraStringFromIntent(intent)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return true
    }

    private fun getExtraStringFromIntent(intent: Intent): String {
        name = intent.getStringExtra("name").orEmpty()
        return name

    }
}