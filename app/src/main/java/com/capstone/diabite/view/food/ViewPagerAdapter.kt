package com.capstone.diabite.view.food

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.diabite.db.responses.FoodItem

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private val clusterTitles = listOf("SUGGESTION", "ALTERNATIVE", "AVOID")
    private val clusterKeys = listOf("cluster_0", "cluster_1", "cluster_2")
    private var foodClusters: Map<String, List<FoodItem>> = emptyMap()

    override fun getItemCount(): Int = clusterTitles.size

    override fun createFragment(position: Int): Fragment {
        val foodItem = foodClusters[clusterKeys[position]] ?: emptyList()
        return FoodFragment.newInstance(foodItem)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateClusters(clusters: Map<String, List<FoodItem>>) {
        foodClusters = clusters
        notifyDataSetChanged()
    }

    fun getPageTitle(position: Int): String = clusterTitles[position]
}
