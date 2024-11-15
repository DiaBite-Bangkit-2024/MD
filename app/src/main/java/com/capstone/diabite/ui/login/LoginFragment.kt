package com.capstone.diabite.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.diabite.databinding.FragmentLoginBinding
import com.capstone.diabite.ui.register.RegisterFragment
import com.capstone.diabite.view.MainActivity
import com.capstone.diabite.view.auth.AuthActivity

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

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

            tvLogin.setOnClickListener {
                // Already in LoginFragment, no action needed
            }

            tvRegister.setOnClickListener {
                (activity as? AuthActivity)?.loadFragment(RegisterFragment())
            }

            loginButton.setOnClickListener {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
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
}
