package com.capstone.diabite.view.auth

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import com.capstone.diabite.ui.register.EmailSender
import com.capstone.diabite.view.InitInfoActivity
import com.faraflh.storyapp.data.pref.UserPreference
import com.faraflh.storyapp.data.pref.dataStore


class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private val loginVM: LoginViewModel by viewModels {
        AuthViewModelFactory(
            UserRepository.getInstance(
                UserPreference.getInstance(dataStore)
            )
        )
    }

    private var email: String = ""
    private var password: String = ""
    private var otp: String = ""

    //    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


//        auth = FirebaseAuth.getInstance()
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

            resend.setOnClickListener {
                otp = generateOtp()
                EmailSender.sendOtpToEmail(this@OtpActivity, email, otp)
//
//                Toast.makeText(
//                    this@OtpActivity,
//                    getString(R.string.otp_sent, email),
//                    Toast.LENGTH_LONG
//                ).show()
            }

            verifBtn.setOnClickListener {
                val enteredOtp = "${otp1.text}${otp2.text}${otp3.text}${otp4.text}"
                loginVM.verifyOtp(email, enteredOtp).observe(this@OtpActivity) { result ->
                    when (result) {
                        is DataResult.Loading -> {}
                        is DataResult.Success -> {
                            val intent = Intent(this@OtpActivity, InitInfoActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        is DataResult.Error -> {
                            Toast.makeText(this@OtpActivity, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

//            verifBtn.setOnClickListener {
//                val enteredOtp = "${otp1.text}${otp2.text}${otp3.text}${otp4.text}"
//                if (enteredOtp.isNotEmpty()) {
//                    lifecycleScope.launch {
//                        userPreference.saveOtp(enteredOtp)
////                        val prefEmail = getEmailFromPreferences()
//                        otpVM.verifyOtp(ApiService.OtpRequest(emailtv, enteredOtp))
//                    }
//                } else {
//                    Toast.makeText(this@OtpActivity, "Please enter OTP", Toast.LENGTH_SHORT).show()
//                }
//                otpVM.verifyOtpResult.observe(this) { result ->
//                    result.onSuccess { otpResult ->
//                        // Successfully verified OTP
//                        Toast.makeText(this, "OTP Verified: ${otpResult.otp}", Toast.LENGTH_SHORT).show()
//                        // Proceed to next activity or flow
//                    }.onFailure { exception ->
//                        // OTP verification failed
//                        Toast.makeText(this, "Verification failed: ${exception.message}", Toast.LENGTH_SHORT).show()
//                    }
//                }}

            otp1.doOnTextChanged { text, start, before, count ->
                if (otp1.text.toString().isNotEmpty()) otp2.requestFocus()
            }

            otp2.doOnTextChanged { text, start, before, count ->
                if (otp2.text.toString().isNotEmpty()) otp3.requestFocus()
            }

            otp3.doOnTextChanged { text, start, before, count ->
                if (otp3.text.toString().isNotEmpty()) otp4.requestFocus()
            }
        }
    }

    private fun generateOtp(): String {
        return (1000..9999).random().toString()
    }
}