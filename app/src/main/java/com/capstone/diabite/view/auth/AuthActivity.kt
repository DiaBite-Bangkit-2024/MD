package com.capstone.diabite.view.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.capstone.diabite.R
import com.capstone.diabite.databinding.ActivityAuthBinding
import com.capstone.diabite.ui.login.LoginFragment
import com.capstone.diabite.ui.register.RegisterFragment
import com.capstone.diabite.view.MainActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true)

        if (isFirstLaunch) {
            // Redirect to OnBoardingActivity
            val intentOnb = Intent(this, OnBoardingActivity::class.java)
            startActivity(intentOnb)
            finish() // Prevent MainActivity from being shown
        }

        if (isUserLoggedIn(this)) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            if (savedInstanceState == null) {
                loadFragment(LoginFragment())
            }
        }
    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.nav_host_fragment_activity_auth, fragment)
            .commit()
    }

    private fun isUserLoggedIn(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("IS_LOGGED_IN", false)
    }
}
