package com.capstone.diabite.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.diabite.R
import com.capstone.diabite.databinding.FragmentLoginBinding
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.ui.register.RegisterFragment
import com.capstone.diabite.view.MainActivity
import com.capstone.diabite.view.auth.AuthActivity
import com.capstone.diabite.view.auth.AuthViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class LoginFragment : Fragment() {

    private var gso: GoogleSignInOptions? = null
    private var gsc: GoogleSignInClient? = null

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

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(requireContext(), gso!!)

        binding.apply {

            tvLogin.setOnClickListener {
            }

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
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is DataResult.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.putExtra("name", result.data.loginResult.name)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                        is DataResult.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            googleLoginButton.setOnClickListener {
                signInWithGoogle()
            }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = gsc?.signInIntent
        if (signInIntent != null) {
            startActivityForResult(signInIntent, 1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                handleSignInResult(account)
            } catch (e: ApiException) {
                Toast.makeText(context, "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSignInResult(account: GoogleSignInAccount?) {
        if (account != null) {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("name", account.displayName)
            intent.putExtra("email", account.email)
            startActivity(intent)
            requireActivity().finish()
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