package com.capstone.diabite.view.food

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Pair
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.diabite.databinding.ActivityRecomBinding
import com.capstone.diabite.db.ApiClient
import com.capstone.diabite.ui.articles.ArticlesRepo
import com.capstone.diabite.ui.articles.ArticlesVMFactory
import com.capstone.diabite.ui.articles.ArticlesViewModel
import com.capstone.diabite.view.MainActivity
import kotlinx.coroutines.flow.collectLatest

class RecomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecomBinding
    private lateinit var foodVM: ArticlesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val tagsAdapter = TagAdapter { tag ->
            val intent = Intent(this, FoodResultActivity::class.java)
            intent.putExtra("TAG", tag)
            val pair = Pair(binding.rvFood as android.view.View, "name")
            val options = android.app.ActivityOptions.makeSceneTransitionAnimation(this, pair)

            startActivity(intent, options.toBundle())
        }
        binding.rvFood.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvFood.adapter = tagsAdapter

        val repository = ArticlesRepo(ApiClient.getApiService2())
        val factory = ArticlesVMFactory(repository)

        foodVM = ViewModelProvider(this, factory)[ArticlesViewModel::class.java]

        foodVM.loadTags()

        lifecycleScope.launchWhenStarted {
            foodVM.tags.collectLatest { tagList ->
                tagsAdapter.submitList(tagList)
            }
        }

        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.etSearchTag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tagsAdapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}