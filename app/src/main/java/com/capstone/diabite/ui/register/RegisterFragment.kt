package com.capstone.diabite.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.diabite.databinding.FragmentRegisterBinding
import com.capstone.diabite.ui.login.LoginFragment
import com.capstone.diabite.view.auth.AuthActivity

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            tvLogin.setOnClickListener {
                (activity as? AuthActivity)?.loadFragment(LoginFragment())
            }

            tvRegister.setOnClickListener {
            }

//            loginToggle.isChecked = false
//            regisToggle.isChecked = true
//
//            loginToggle.setOnClickListener {
//                if (!loginToggle.isChecked) {
//                    loginToggle.isChecked = true
//                    regisToggle.isChecked = false
//                }
//                (activity as? AuthActivity)?.loadFragment(LoginFragment())
//            }
//
//            regisToggle.setOnClickListener {
//                if (!regisToggle.isChecked) {
//                    regisToggle.isChecked = true
//                    loginToggle.isChecked = false
//                }
//            }
        }
    }
}