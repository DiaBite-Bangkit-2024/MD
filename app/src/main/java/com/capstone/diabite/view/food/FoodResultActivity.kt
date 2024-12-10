package com.capstone.diabite.view.food

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstone.diabite.databinding.ActivityFoodResultBinding
import com.capstone.diabite.db.ApiClient
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.db.responses.TagsRequest
import com.capstone.diabite.ui.articles.ArticlesRepo
import com.capstone.diabite.ui.articles.ArticlesVMFactory
import com.capstone.diabite.ui.articles.ArticlesViewModel
import com.google.android.material.tabs.TabLayoutMediator

class FoodResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodResultBinding
    private lateinit var foodVM: ArticlesViewModel
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        window.sharedElementEnterTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.move)

        val repository = ArticlesRepo(ApiClient.getApiService2())
        val factory = ArticlesVMFactory(repository)
        foodVM = ViewModelProvider(this, factory)[ArticlesViewModel::class.java]

        val tag = intent.getStringExtra("TAG") ?: "DefaultTag"
        binding.tvTitle.text = "$tag"

        val request = TagsRequest(tags = listOf(tag))
        foodVM.fetchFoodByTag(request)

        foodVM.foodClusters.observe(this) { result ->
            when (result) {
                is DataResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is DataResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val clusters = result.data
                    if (!::viewPagerAdapter.isInitialized) {
                        viewPagerAdapter = ViewPagerAdapter(this).apply {
                            updateClusters(clusters)
                            binding.viewPager.adapter = this
                            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                                tab.text = getPageTitle(position)
                            }.attach()
                        }
                    } else {
                        viewPagerAdapter.updateClusters(clusters)
                    }
                }
                is DataResult.Error -> binding.progressBar.visibility = View.GONE
            }
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
}
