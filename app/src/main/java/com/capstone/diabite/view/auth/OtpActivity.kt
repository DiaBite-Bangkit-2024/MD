package com.capstone.diabite.view.auth

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.capstone.diabite.R
import com.capstone.diabite.databinding.ActivityOtpBinding
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.db.pref.UserRepository
import com.capstone.diabite.ui.login.LoginViewModel
import com.capstone.diabite.view.InitInfoActivity
import com.capstone.diabite.db.pref.UserPreference
import com.capstone.diabite.db.pref.dataStore
class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private val loginVM: LoginViewModel by viewModels {
        AuthViewModelFactory(
            UserRepository.getInstance(
                UserPreference.getInstance(dataStore)
            )
        )
    }

    private var name: String = ""
    private var email: String = ""
    private var password: String = ""
    private var otp: String = ""

    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 60000

    private val otpInputs by lazy {
        listOf(binding.otp1, binding.otp2, binding.otp3, binding.otp4)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        name = intent.getStringExtra("name").orEmpty()
        email = intent.getStringExtra("email").orEmpty()
        password = intent.getStringExtra("pass").orEmpty()
        otp = intent.getStringExtra("otp").orEmpty()

        binding.apply {
            backButton.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            animationView.setAnimation(R.raw.otp2)
            animationView.playAnimation()


            tvOtp.text = String.format(getString(R.string.tvotp, email))
            resend.text = Html.fromHtml(getString(R.string.resend))

            startOtpTimer()

            resend.setOnClickListener {
                if (timeLeftInMillis <= 0) {
                    loginVM.resendOtp(email, otp).observe(this@OtpActivity) { result ->
                        when (result) {
                            is DataResult.Loading -> {}
                            is DataResult.Success -> {
                                Toast.makeText(this@OtpActivity, "OTP resent successfully!", Toast.LENGTH_SHORT).show()
                                timeLeftInMillis = 60000
                                startOtpTimer()
                            }
                            is DataResult.Error -> {
                                Toast.makeText(this@OtpActivity, result.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                } else {
                    Toast.makeText(this@OtpActivity, "Please wait until the timer ends.", Toast.LENGTH_SHORT).show()
                }
            }

            verifBtn.setOnClickListener {
                val enteredOtp = "${otp1.text}${otp2.text}${otp3.text}${otp4.text}"
                loginVM.verifyOtp(email, enteredOtp).observe(this@OtpActivity) { result ->
                    when (result) {
                        is DataResult.Loading -> {}
                        is DataResult.Success -> {
                            val intent = Intent(this@OtpActivity, InitInfoActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.putExtra("email", email)
                            intent.putExtra("name", name)
                            startActivity(intent)
                            finish()
                        }
                        is DataResult.Error -> {
                            Toast.makeText(this@OtpActivity, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            setupOtpInputNavigation()
        }
    }
    private fun setupOtpInputNavigation() {
        otpInputs.forEachIndexed { index, editText ->
            editText.doOnTextChanged { text, _, _, _ ->
                if (!text.isNullOrEmpty() && index < otpInputs.size - 1) {
                    otpInputs[index + 1].requestFocus()
                }
            }
            editText.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL && editText.text.isNullOrEmpty() && index > 0) {
                    otpInputs[index - 1].requestFocus()
                }
                false
            }
        }
    }

    private fun startOtpTimer() {
        countDownTimer?.cancel()
        binding.resend.isEnabled = false
        binding.resend.setTextColor(resources.getColor(R.color.gray))
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                timeLeftInMillis = 0
                updateTimerText()
                binding.resend.setTextAppearance(R.style.highlight)
                binding.resend.isEnabled = true
            }
        }.start()
    }

    private fun updateTimerText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeFormatted = String.format("%02d:%02d", minutes, seconds)
        binding.timer.text = timeFormatted
        binding.resend.isEnabled = timeLeftInMillis <= 0
    }
}