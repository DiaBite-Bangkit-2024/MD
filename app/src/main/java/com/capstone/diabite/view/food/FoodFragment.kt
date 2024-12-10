package com.capstone.diabite.view.food

import androidx.fragment.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.diabite.databinding.FragmentFoodBinding
import com.capstone.diabite.db.responses.FoodItem

class FoodFragment : Fragment() {

    private lateinit var binding: FragmentFoodBinding
    private val foodList: List<FoodItem> by lazy {
        arguments?.getParcelableArrayList("data") ?: emptyList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoodBinding.inflate(inflater, container, false)

        setupRecyclerView()
        updateUIState()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvFood.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFood.adapter = FoodAdapter(foodList) { foodItem ->
            startActivity(Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.FOOD_ID, foodItem)
            })
        }
    }

    private fun updateUIState() {
        with(binding) {
            emptyTextView.visibility = if (foodList.isEmpty()) View.VISIBLE else View.GONE
            rvFood.visibility = if (foodList.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    companion object {
        fun newInstance(clusterData: List<FoodItem>) = FoodFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList("data", ArrayList(clusterData))
            }
        }
    }

}
