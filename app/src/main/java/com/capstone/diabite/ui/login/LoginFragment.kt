package com.capstone.diabite.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.capstone.diabite.R
import com.capstone.diabite.databinding.FragmentLoginBinding
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.ui.register.RegisterFragment
import com.capstone.diabite.view.MainActivity
import com.capstone.diabite.view.auth.AuthActivity
import com.capstone.diabite.view.auth.AuthViewModelFactory
import com.capstone.diabite.view.auth.ForgetActivity

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginVM by viewModels<LoginViewModel> { AuthViewModelFactory.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            tvRegister.setOnClickListener {
                highlightTab(isLoginSelected = true)
                (activity as? AuthActivity)?.loadFragment(RegisterFragment())
            }

            setupEditText()
            setupAction()
            loginButton.setOnClickListener {
                val email = emailEditText.text.toString().trim()
                val pass = passwordEditText.text.toString().trim()
                loginVM.login(email, pass).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is DataResult.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is DataResult.Success -> {
                            progressBar.visibility = View.GONE
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.putExtra("name", result.data.loginResult.name)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                        is DataResult.Error -> {
                            progressBar.visibility = View.GONE
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            forgetpw.setOnClickListener{
                val email = emailEditText.text.toString().trim()
                loginVM.forgotPass(email).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is DataResult.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is DataResult.Success -> {
                            progressBar.visibility = View.GONE
                            val intent = Intent(requireContext(), ForgetActivity::class.java)
                            intent.putExtra("email", email)
                            startActivity(intent)
                        }
                        is DataResult.Error -> {
                            progressBar.visibility = View.GONE
                            Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupEditText() {
        binding.emailEditText.isEmail = true
        binding.passwordEditText.isPassword = true
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (validateInput(email, password)) {
                loginVM.login(email, password)
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        binding.emailEditTextLayout.error = if (email.isEmpty()) getString(R.string.email_required) else null
        binding.passwordEditTextLayout.error = if (password.isEmpty()) getString(R.string.password_required) else null

        return binding.emailEditTextLayout.error == null &&
                binding.passwordEditTextLayout.error == null
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