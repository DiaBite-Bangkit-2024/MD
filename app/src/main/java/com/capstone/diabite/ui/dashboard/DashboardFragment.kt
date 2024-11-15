package com.capstone.diabite.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.diabite.databinding.FragmentDashboardBinding
import com.capstone.diabite.view.AnalyzeActivity
import com.capstone.diabite.view.HistoryActivity
import com.capstone.diabite.view.MainActivity

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.apply {
            logoutBtn.setOnClickListener {


            }
            val progress = 20  // Set this value based on your data
            binding.circularProgressView.setProgress(progress)
            binding.progressText.text = "$progress%"

            btnAnalyze.setOnClickListener {
                val intent = Intent(context, AnalyzeActivity::class.java)
                startActivity(intent)
            }
            btnRec.setOnClickListener {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            }
            btnHistory.setOnClickListener {
                val intent = Intent(context, HistoryActivity::class.java)
                startActivity(intent)
            }

        }




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}