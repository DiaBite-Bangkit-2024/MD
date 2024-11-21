package com.capstone.diabite.ui.register

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.capstone.diabite.R
import com.capstone.diabite.databinding.FragmentRegisterBinding
import com.capstone.diabite.ui.login.LoginFragment
import com.capstone.diabite.view.auth.AuthActivity
import com.capstone.diabite.view.auth.OtpActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private var otp: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        binding.apply {
            // Switch to Login tab
            tvLogin.setOnClickListener {
                highlightTab(isLoginSelected = true)
                (activity as? AuthActivity)?.loadFragment(LoginFragment())
            }

            // Handle Register button click
            regisButton.setOnClickListener {
                val email = emailEditText.text.toString().trim()
                val password = passwordEditText.text.toString().trim()

                // Validate input fields
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        context,
                        getString(R.string.fill_all),
                        Toast.LENGTH_LONG
                    ).show()
                    //TODO: redirect to login, add timer
                } else {
                    otp = generateOtp()
                    EmailSender.sendOtpToEmail(requireContext(), email, otp)

                    val intent = Intent(context, OtpActivity::class.java).apply {
                        putExtra("email", email)
                        putExtra("pass", password)
                        putExtra("otp", otp)
                    }
                    startActivity(intent)
                }
            }
        }
    }

    private fun generateOtp(): String {
        return (1000..9999).random().toString()
    }

//    fun sendOtpToEmail(email: String) {
//        val otp = generateOtp() // Replace with your OTP generation logic
//        val subject = "DiaBite Signup OTP"
//        val message = "Your OTP is $otp"
//
////        GMailSender("diabite.b7@gmail.com", "aahensrvxrkoidtv", email, subject, message).execute()
//
//        val sender = GMailSender("diabite.b7@gmail.com", "aahensrvxrkoidtv") // Replace with your app password
//
//        CoroutineScope(Dispatchers.Main).launch {
//            val result = sender.sendEmail(email, subject, message)
//            result.fold(
//                onSuccess = {
//                    Toast.makeText(context, "OTP sent to $email. Check your inbox.", Toast.LENGTH_LONG).show()
//                },
//                onFailure = { e ->
//                    Log.e("GMailSender", "Failed to send OTP", e)
//                    Toast.makeText(context, "Failed to send OTP: ${e.message}", Toast.LENGTH_LONG).show()
//                }
//            )
//        }
//    }



    private fun highlightTab(isLoginSelected: Boolean) {
        binding.tvLogin.setBackgroundResource(
            if (isLoginSelected) R.drawable.selected_tab_background else android.R.color.transparent
        )
        binding.tvRegister.setBackgroundResource(
            if (!isLoginSelected) R.drawable.selected_tab_background else android.R.color.transparent
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}