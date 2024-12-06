package com.capstone.diabite.db.local

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.diabite.R
import com.capstone.diabite.databinding.ItemHistoryBinding

class HistoryAdapter(private val context: Context, var historyList: List<HistoryEntity>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyItem = historyList[position]
        holder.bind(historyItem)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(historyItem: HistoryEntity) {
            binding.apply {
                tvItemName.setText(R.string.result_text)
                tvItemDescription.text = historyItem.summary
                circularProgressView.setProgress(historyItem.prediction)
                progressText.text = binding.root.context.getString(
                    R.string.progress_text, historyItem.prediction
                )
            }
//            binding.progressText.text = "${historyItem.prediction}%"
        }
    }
}
