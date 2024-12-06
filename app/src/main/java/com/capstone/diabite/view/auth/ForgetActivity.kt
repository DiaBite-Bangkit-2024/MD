package com.capstone.diabite.view.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.diabite.R
import com.capstone.diabite.databinding.ActivityForgetBinding
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.db.pref.UserPreference
import com.capstone.diabite.db.pref.UserRepository
import com.capstone.diabite.db.pref.dataStore
import com.capstone.diabite.ui.login.LoginViewModel

class ForgetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetBinding
    private val loginVM: LoginViewModel by viewModels {
        AuthViewModelFactory(
            UserRepository.getInstance(
                UserPreference.getInstance(dataStore)
            )
        )
    }
    private var email: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        email = intent.getStringExtra("email").orEmpty()

        setupEditText()
        setupAction()

        binding.apply {
            backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
            tvOtp.text = String.format(getString(R.string.tvotp, email))
        }
    }

    private fun setupEditText() {
        binding.apply {
            emailEditText.isEmail = true
            passwordEditText.isPassword = true
            confirmPasswordEditText.isConfirm = true
            otpEditText.isOtp = true
            confirmPasswordEditText.passwordReference = passwordEditText
        }
    }

    private fun setupAction() {
        binding.apply {
            resetBtn.setOnClickListener {
                val enteredOtp = otpEditText.text.toString().trim()
                val email = emailEditText.text.toString().trim()
                val pass = confirmPasswordEditText.text.toString().trim()

                if (validateInput(email, enteredOtp, pass)) {
                    loginVM.resetPass(email, enteredOtp, pass)
                }

                loginVM.resetPassword.observe(this@ForgetActivity) { result ->
                    when (result) {
                        is DataResult.Loading -> {}
                        is DataResult.Success -> {
                            val response = result.data
                            if (!response.error) {
                                Toast.makeText(this@ForgetActivity, "Password reset successful: ${response.message}", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@ForgetActivity, "Password reset failed: ${response.message}", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@ForgetActivity, AuthActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                        }

                        is DataResult.Error -> {
                            Toast.makeText(this@ForgetActivity, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }
    }

    private fun validateInput(email: String, otp: String, password: String): Boolean {
        val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()
        binding.emailEditTextLayout.error = if (email.isEmpty()) getString(R.string.email_required) else null
        binding.otpEditTextLayout.error = if (otp.isEmpty()) getString(R.string.otp_required) else null
        binding.passwordEditTextLayout.error = if (password.isEmpty()) getString(R.string.password_required) else null
        binding.confirmPasswordEditTextLayout.error = if (confirmPassword.isEmpty()) getString(R.string.confirm_required) else null

        return binding.emailEditTextLayout.error == null && binding.otpEditTextLayout.error == null && binding.passwordEditTextLayout.error == null && binding.confirmPasswordEditTextLayout.error == null
    }
}