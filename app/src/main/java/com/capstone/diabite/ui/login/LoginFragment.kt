package com.capstone.diabite.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.capstone.diabite.R
import com.capstone.diabite.databinding.FragmentLoginBinding
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.db.pref.UserRepository
import com.capstone.diabite.ui.register.RegisterFragment
import com.capstone.diabite.view.InitInfoActivity
import com.capstone.diabite.view.MainActivity
import com.capstone.diabite.view.auth.AuthActivity
import com.capstone.diabite.view.auth.AuthViewModelFactory
import com.faraflh.storyapp.data.pref.UserPreference
import com.faraflh.storyapp.data.pref.dataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
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
