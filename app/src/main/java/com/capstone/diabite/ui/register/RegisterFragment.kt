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
import androidx.fragment.app.viewModels
import com.capstone.diabite.R
import com.capstone.diabite.databinding.FragmentRegisterBinding
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.ui.login.LoginFragment
import com.capstone.diabite.ui.login.LoginViewModel
import com.capstone.diabite.view.MainActivity
import com.capstone.diabite.view.auth.AuthActivity
import com.capstone.diabite.view.auth.AuthViewModelFactory
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
    private val loginVM by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(
            requireContext()
        )
    }

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

            setupEditText()
            setupAction()

            // Handle Register button click
            regisButton.setOnClickListener {
                val name = nameEditText.text.toString().trim()
                val email = emailEditText.text.toString().trim()
                val pass = passwordEditText.text.toString().trim()

                loginVM.register(name, email, pass).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is DataResult.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is DataResult.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val intent = Intent(requireContext(), OtpActivity::class.java).apply {
                                putExtra("name", name)
                                putExtra("email", email)
                                putExtra("pass", pass)
                            }
                            startActivity(intent)
                        }

                        is DataResult.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

//                if (email.isEmpty() || password.isEmpty()) {
//                    Toast.makeText(
//                        context,
//                        getString(R.string.fill_all),
//                        Toast.LENGTH_LONG
//                    ).show()
//                    //TODO: redirect to login, add timer
//                } else {
//                    otp = generateOtp()
//                    EmailSender.sendOtpToEmail(requireContext(), email, otp)
//
//                    val intent = Intent(context, OtpActivity::class.java).apply {
//                        putExtra("email", email)
//                        putExtra("pass", password)
//                        putExtra("otp", otp)
//                    }
//                    startActivity(intent)
//                }
            }
        }
    }

    private fun generateOtp(): String {
        return (1000..9999).random().toString()
    }

    private fun setupEditText() {
        binding.nameEditText.isName = true
        binding.emailEditText.isEmail = true
        binding.passwordEditText.isPassword = true
    }

    private fun setupAction() {
        binding.regisButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (validateInput(name, email, password)) {
                loginVM.register(name, email, password)
            }
        }
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        binding.nameEditTextLayout.error = if (name.isEmpty()) getString(R.string.name_required) else null
        binding.emailEditTextLayout.error = if (email.isEmpty()) getString(R.string.email_required) else null
        binding.passwordEditTextLayout.error = if (password.isEmpty()) getString(R.string.password_required) else null

        return binding.nameEditTextLayout.error == null &&
                binding.emailEditTextLayout.error == null &&
                binding.passwordEditTextLayout.error == null
    }

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