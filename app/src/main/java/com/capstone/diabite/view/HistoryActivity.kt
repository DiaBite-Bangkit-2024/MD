package com.capstone.diabite.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.diabite.databinding.ActivityHistoryBinding
import com.capstone.diabite.db.local.HistoryAdapter
import com.capstone.diabite.db.local.HistoryViewModel

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyVM: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val historyAdapter = HistoryAdapter(this, listOf())
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = historyAdapter

        historyVM = ViewModelProvider(this)[HistoryViewModel::class.java]

        historyVM.allHistory.observe(this, Observer { historyList ->
            if (historyList.isEmpty()) {
                Toast.makeText(this, "No history data available", Toast.LENGTH_SHORT).show()

            } else {
                binding.rvHistory.visibility = View.VISIBLE
                historyAdapter.historyList = historyList
                historyAdapter.notifyDataSetChanged()

            }
        })

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
}