package com.capstone.diabite.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.capstone.diabite.R
import com.capstone.diabite.databinding.FragmentLoginBinding
import com.capstone.diabite.ui.register.RegisterFragment
import com.capstone.diabite.view.InitInfoActivity
import com.capstone.diabite.view.MainActivity
import com.capstone.diabite.view.auth.AuthActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        binding.apply {

            tvLogin.setOnClickListener {
                // Already in LoginFragment, no action needed
            }

            tvRegister.setOnClickListener {
                highlightTab(isLoginSelected = true)
                (activity as? AuthActivity)?.loadFragment(RegisterFragment())
            }

            loginButton.setOnClickListener {
                val email = emailEditText.text.toString().trim()
                val pass = passwordEditText.text.toString().trim()

                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(context as Activity) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val intent = Intent(context, MainActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            onDestroyView()
                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.login_failed),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }

//            tvLogin.isChecked = true
//            tvRegister.isChecked = false
//
//            tvLogin.setOnClickListener {
//                if (!tvLogin.isChecked) {
//                    tvLogin.isChecked = true
//                    tvRegister.isChecked = false
//                }
//                // Already in LoginFragment, no action needed
//            }
//
//            tvRegister.setOnClickListener {
//                if (!tvRegister.isChecked) {
//                    tvRegister.isChecked = true
//                    tvLogin.isChecked = false
//                }
//                // Switch to RegisterFragment
//                (activity as? AuthActivity)?.loadFragment(RegisterFragment())
//            }
        }
    }

    private fun highlightTab(isLoginSelected: Boolean) {
        binding.tvLogin.setBackgroundResource(
            if (!isLoginSelected) R.drawable.selected_tab_background else android.R.color.transparent
        )
        binding.tvRegister.setBackgroundResource(
            if (isLoginSelected) R.drawable.selected_tab_background else android.R.color.transparent
        )
    }
}
