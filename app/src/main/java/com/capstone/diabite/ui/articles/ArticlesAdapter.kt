package com.capstone.diabite.ui.articles

import android.content.Intent
import android.net.Uri
import com.capstone.diabite.databinding.ItemArticlesBinding
import com.capstone.diabite.db.responses.NewsResultsItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ArticlesAdapter : ListAdapter<NewsResultsItem, ArticlesAdapter.NewsViewHolder>(DIFF_CALLBACK)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
    }

    inner class NewsViewHolder(private val binding: ItemArticlesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: NewsResultsItem) {
            binding.apply {
                Glide.with(root.context)
                    .load(article.thumbnail)
                    .into(img)
                tvItemName.text = article.title

                root.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
                    root.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsResultsItem>() {
            override fun areItemsTheSame(oldItem: NewsResultsItem, newItem: NewsResultsItem): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: NewsResultsItem, newItem: NewsResultsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}