package com.capstone.diabite.ui.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.diabite.databinding.FragmentArticlesBinding
import com.capstone.diabite.db.ApiClient
import com.capstone.diabite.db.DataResult

class ArticlesFragment : Fragment() {

    private var _binding: FragmentArticlesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: ArticlesViewModel
    private val articlesAdapter = ArticlesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = ArticlesRepo(ApiClient.getApiService())
        viewModel = ViewModelProvider(this, ArticlesVMFactory(repository))[ArticlesViewModel::class.java]

        setupRecyclerView()
        observeNewsData()
        fetchNews()
    }

    private fun setupRecyclerView() {
        binding.rvArticles.apply {
            adapter = articlesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeNewsData() {
        viewModel.newsData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DataResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is DataResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    articlesAdapter.submitList(result.data.take(30))
                }
                is DataResult.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchNews() {
        val query = "diabetes" // Replace with dynamic input if needed
        viewModel.fetchNews(query)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}