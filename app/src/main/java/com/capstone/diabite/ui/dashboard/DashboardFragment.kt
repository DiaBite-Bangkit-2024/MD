package com.capstone.diabite.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.capstone.diabite.R
import com.capstone.diabite.databinding.FragmentDashboardBinding
import com.capstone.diabite.ui.login.LoginViewModel
import com.capstone.diabite.view.AnalyzeActivity
import com.capstone.diabite.view.HistoryActivity
import com.capstone.diabite.view.MainActivity
import com.capstone.diabite.view.RecomActivity
import com.capstone.diabite.view.auth.AuthViewModelFactory
import java.util.Calendar

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!
    private val loginVM by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(
            requireContext()
        )
    }
//    private var name: String = ""

//
//    private lateinit var nameTextView: TextView
//    private lateinit var ageTextView: TextView
//    private lateinit var heightTextView: TextView
//    private lateinit var weightTextView: TextView
//    private lateinit var bloodPressureTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        //
//        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
//
//        nameTextView = view.findViewById(R.id.dbName)
//        ageTextView = view.findViewById(R.id.vAge)
//        heightTextView = view.findViewById(R.id.tHeight)
//        weightTextView = view.findViewById(R.id.tWeight)
//        bloodPressureTextView = view.findViewById(R.id.tBP)
//
//        loadUserData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString("name")
        binding.dbName.text = String.format(getString(R.string.db_name, name))

        binding.apply {
            logoutBtn.setOnClickListener {
                loginVM.logout()
            }

            val greetingMessage = getGreetingMessage()

            // Update the TextView
            dbGreeting.text = greetingMessage

            val progress = 20  // Set this value based on your data
            circularProgressView.setProgress(progress)
            progressText.text = "$progress%"

            btnAnalyze.setOnClickListener {
                val intent = Intent(context, AnalyzeActivity::class.java)
                startActivity(intent)
            }
            btnRec.setOnClickListener {
                val intent = Intent(context, RecomActivity::class.java)
                startActivity(intent)
            }
            btnHistory.setOnClickListener {
                val intent = Intent(context, HistoryActivity::class.java)
                startActivity(intent)
            }

        }
    }

    fun getGreetingMessage(): String {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (currentHour) {
            in 5..11 -> getString(R.string.good_morning)
            in 12..17 -> getString(R.string.good_afternoon)
            in 18..22 -> getString(R.string.good_evening)
            else -> getString(R.string.good_night)
        }
    }

    override fun onResume() {
        super.onResume()
//        loadUserData()
    }

//    private fun loadUserData() {
//        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
//        val name = sharedPreferences.getString("name", "Name")
//        nameTextView.text = name
//
//        val age = sharedPreferences.getInt("age", 0)
//        ageTextView.text = "$age y.o"
//
//        val height = sharedPreferences.getFloat("height", 0f)
//        heightTextView.text = "$height cm"
//
//        val weight = sharedPreferences.getFloat("weight", 0f)
//        weightTextView.text = "$weight kg"
//
//        val bloodPressure = sharedPreferences.getString("blood_pressure", "0/0")
//        bloodPressureTextView.text = bloodPressure
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}