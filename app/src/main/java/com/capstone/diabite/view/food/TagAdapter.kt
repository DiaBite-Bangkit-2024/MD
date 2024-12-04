package com.capstone.diabite.view.food

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.diabite.databinding.ItemFoodBinding

class TagAdapter(private val onItemClickListener: (String) -> Unit) : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    private var fullList: List<String> = listOf()
    private var filteredList: MutableList<String> = mutableListOf()

    fun submitList(tags: List<String>) {
        fullList = tags
        filteredList = tags.toMutableList()
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        if (query.isEmpty()) {
            filteredList = fullList.toMutableList()
        } else {
            filteredList = fullList.filter { it.contains(query, ignoreCase = true) }.toMutableList()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFoodBinding.inflate(inflater, parent, false)
        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = filteredList[position]
        holder.bind(tag)
        holder.itemView.setOnClickListener {
            onItemClickListener(tag)
        }
    }

    override fun getItemCount(): Int = filteredList.size

    inner class TagViewHolder(private val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tag: String) {
            binding.tvFoodName.text = tag
        }
    }
}


