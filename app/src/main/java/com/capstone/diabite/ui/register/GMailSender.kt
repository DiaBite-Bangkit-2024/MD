package com.capstone.diabite.ui.register



import android.os.AsyncTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class GMailSender(
    private val senderEmail: String,
    private val senderPassword: String
) {

    suspend fun sendEmail(recipientEmail: String, subject: String, message: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val props = Properties().apply {
                    put("mail.smtp.host", "smtp.gmail.com")
                    put("mail.smtp.port", "587")
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.starttls.enable", "true")
                }

                val session = Session.getInstance(props, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(senderEmail, senderPassword)
                    }
                })

                val mimeMessage = MimeMessage(session).apply {
                    setFrom(InternetAddress(senderEmail))
                    setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail))
                    this.subject = subject
                    setText(message)
                }

                Transport.send(mimeMessage)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}


//class GMailSender(private val email: String, private val password: String) {
//
//    fun sendMailAsync(
//        subject: String,
//        body: String,
//        sender: String,
//        recipient: String,
//        callback: (Boolean, String) -> Unit
//    ) {
//        SendMailTask(email, password, subject, body, sender, recipient, callback).execute()
//    }
//
//    private class SendMailTask(
//        private val email: String,
//        private val password: String,
//        private val mSubject: String,
//        private val body: String,
//        private val senderEmail: String,
//        private val recipient: String,
//        private val callback: (Boolean, String) -> Unit
//    ) : AsyncTask<Void, Void, Pair<Boolean, String>>() {
//
//        override fun doInBackground(vararg params: Void?): Pair<Boolean, String> {
//            return try {
//                val props = Properties().apply {
//                    put("mail.smtp.host", "smtp.gmail.com")
//                    put("mail.smtp.socketFactory.port", "465")
//                    put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
//                    put("mail.smtp.auth", "true")
//                    put("mail.smtp.port", "465")
//                }
//
//                val session = Session.getInstance(props, object : Authenticator() {
//                    override fun getPasswordAuthentication() =
//                        PasswordAuthentication(email, password)
//                })
//
//                val message = MimeMessage(session).apply {
//                    setFrom(InternetAddress(senderEmail))
//                    setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(recipient))
//                    setSubject(mSubject)
//                    setText(body)
//                }
//
//                Transport.send(message)
//                Pair(true, "Email sent successfully")
//            } catch (e: Exception) {
//                Pair(false, e.message ?: "Unknown error occurred")
//            }
//        }
//
//        override fun onPostExecute(result: Pair<Boolean, String>) {
//            callback(result.first, result.second)
//        }
//    }
//}

//class GMailSender(
//    private val senderEmail: String,
//    private val senderPassword: String,
//    private val recipientEmail: String,
//    private val subject: String,
//    private val message: String
//) : AsyncTask<Void, Void, Void>() {
//
//    override fun doInBackground(vararg params: Void?): Void? {
//        val props = Properties()
//        props["mail.smtp.host"] = "smtp.gmail.com"
//        props["mail.smtp.port"] = "587"
//        props["mail.smtp.auth"] = "true"
//        props["mail.smtp.starttls.enable"] = "true"
//
//        val session = Session.getInstance(props, object : Authenticator() {
//            override fun getPasswordAuthentication(): PasswordAuthentication {
//                return PasswordAuthentication(senderEmail, senderPassword)
//            }
//        })
//
//        try {
//            val mimeMessage = MimeMessage(session)
//            mimeMessage.setFrom(InternetAddress(senderEmail))
//            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail))
//            mimeMessage.subject = subject
//            mimeMessage.setText(message)
//            Transport.send(mimeMessage)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return null
//    }
//}