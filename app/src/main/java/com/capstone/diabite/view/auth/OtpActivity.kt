package com.capstone.diabite.view.auth

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.capstone.diabite.R
import com.capstone.diabite.databinding.ActivityOtpBinding
import com.capstone.diabite.ui.register.EmailSender
import com.capstone.diabite.ui.register.RegisterFragment
import com.capstone.diabite.view.InitInfoActivity
import com.google.firebase.auth.FirebaseAuth
import kotlin.random.Random
import kotlin.random.nextInt


class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private var email: String = ""
    private var password: String = ""
    private var otp: String = ""
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
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
                if (enteredOtp == otp) {
                    // Proceed with registration
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this@OtpActivity) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@OtpActivity,
                                    getString(R.string.registration_successful),
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(this@OtpActivity, InitInfoActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@OtpActivity,
                                    getString(R.string.registration_failed),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this@OtpActivity,
                        getString(R.string.otp_invalid),
                        Toast.LENGTH_LONG
                    ).show()
                }
//                val dialog = Dialog(this@OtpActivity)
//                val dialogView = layoutInflater.inflate(R.layout.popup_dialog, null)
//
//                // Wrap the dialog's content in a FrameLayout to apply margins
//                val marginLayout = FrameLayout(this@OtpActivity)
//                val params = FrameLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//                )
//                params.setMargins(20, 0, 20, 0) // Set desired margins (left, top, right, bottom)
//                dialogView.layoutParams = params
//
//                marginLayout.addView(dialogView)
//                dialog.setContentView(marginLayout)
//                dialog.window?.setLayout(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//                )
//                dialog.setCancelable(false)
//                dialog.window?.attributes?.windowAnimations = R.style.animation
//                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//
//                val okay_text: TextView = dialog.findViewById(R.id.okay_text)
//                val cancel_text: TextView = dialog.findViewById(R.id.cancel_text)
//                val close: ImageButton = dialog.findViewById(R.id.close_button)
//
//                okay_text.setOnClickListener {
//                    dialog.dismiss()
//                }
//
//                cancel_text.setOnClickListener {
//                    dialog.dismiss()
//                }
//
//                close.setOnClickListener {
//                    dialog.dismiss()
//                }
//
//                dialog.show()
            }

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