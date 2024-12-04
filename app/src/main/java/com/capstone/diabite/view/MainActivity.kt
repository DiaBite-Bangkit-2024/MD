package com.capstone.diabite.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.diabite.R
import com.capstone.diabite.databinding.ActivityMainBinding
import com.capstone.diabite.ui.dashboard.DashboardFragment
import com.capstone.diabite.ui.dashboard.DashboardViewModel
import com.capstone.diabite.ui.login.LoginViewModel
import com.capstone.diabite.view.auth.AuthActivity
import com.capstone.diabite.view.auth.AuthViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
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
                startActivity(Intent(this, AuthActivity::class.java))
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

    private fun getExtraStringFromIntent(intent: Intent): String? {
        name = intent.getStringExtra("name").orEmpty()
        return name

    }

//    private fun setupSmoothBottomMenu() {
//        val popupMenu = PopupMenu(this, null)
//        popupMenu.inflate(R.menu.bottom_nav_menu)
//        val menu = popupMenu.menu
//        navView.setupWithNavController(menu, navController)
////        binding.navView.setupWithNavController(navController)
//    }
}