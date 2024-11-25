package com.capstone.diabite.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.diabite.R
import com.capstone.diabite.databinding.ActivityMainBinding
import com.capstone.diabite.ui.login.LoginViewModel
import com.capstone.diabite.view.auth.AuthActivity
import com.capstone.diabite.view.auth.AuthViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(this)
    }
//
//    private var name: String = ""
//    private var age: String = ""
//    private var height: String = ""
//    private var weight: String = ""
//    private var systolic: String = ""
//    private var diastolic: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard, R.id.navigation_articles, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }
//
//        name = intent.getStringExtra("name").orEmpty()
//        age = intent.getStringExtra("age").orEmpty()
//        height = intent.getStringExtra("height").orEmpty()
//        weight = intent.getStringExtra("weight").orEmpty()
//        systolic = intent.getStringExtra("systolic").orEmpty()
//        diastolic = intent.getStringExtra("diastolic").orEmpty()

    }

}