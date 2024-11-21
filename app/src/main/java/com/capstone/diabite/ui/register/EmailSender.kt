package com.capstone.diabite.ui.register

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object EmailSender {
    fun sendOtpToEmail(context: Context, email: String, otp: String) {
        val subject = "DiaBite Signup OTP"
        val message = "Your OTP is $otp"

        val sender = GMailSender("diabite.b7@gmail.com", "aahensrvxrkoidtv") // Replace with your app password

        CoroutineScope(Dispatchers.IO).launch {
            val result = sender.sendEmail(email, subject, message)
            withContext(Dispatchers.Main) {
                result.fold(
                    onSuccess = {
                        Toast.makeText(context, "OTP sent to $email. Check your inbox.", Toast.LENGTH_LONG).show()
                    },
                    onFailure = { e ->
                        Log.e("GMailSender", "Failed to send OTP", e)
                        Toast.makeText(context, "Failed to send OTP: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }
}
