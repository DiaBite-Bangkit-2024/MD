package com.capstone.diabite.view

import android.R
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.capstone.diabite.databinding.ActivitySplashBinding
import com.capstone.diabite.ui.login.LoginViewModel
import com.capstone.diabite.view.auth.AuthActivity
import com.capstone.diabite.view.auth.AuthViewModelFactory


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        anim()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            viewModel.getSession().observe(this) { user ->
                if (!user.isLogin) {
                    startActivity(Intent(this, AuthActivity::class.java))
                    finish()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 3000)
    }

    private fun anim() {
        val imageView = binding.splashIc
        val zoomInX = ObjectAnimator.ofFloat(imageView, "scaleX", 1.0f, 1.2f)
        val zoomInY = ObjectAnimator.ofFloat(imageView, "scaleY", 1.0f, 1.2f)
        zoomInX.duration = 1000
        zoomInY.duration = 1000

        val zoomOutX = ObjectAnimator.ofFloat(imageView, "scaleX", 1.2f, 1.0f)
        val zoomOutY = ObjectAnimator.ofFloat(imageView, "scaleY", 1.2f, 1.0f)
        zoomOutX.duration = 1000
        zoomOutY.duration = 1000

        val zoomInSet = AnimatorSet()
        zoomInSet.playTogether(zoomInX, zoomInY)

        val zoomOutSet = AnimatorSet()
        zoomOutSet.playTogether(zoomOutX, zoomOutY)

        val repeatingAnimator = AnimatorSet()
        repeatingAnimator.playSequentially(zoomInSet, zoomOutSet)
        repeatingAnimator.start()

        repeatingAnimator.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                repeatingAnimator.start()
            }
        })

    }
}