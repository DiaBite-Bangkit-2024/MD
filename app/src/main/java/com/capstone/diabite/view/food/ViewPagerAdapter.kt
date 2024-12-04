package com.capstone.diabite.view.food

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private val clusterTitles = listOf("SUGGESTION", "ALTERNATIVE", "AVOID")
    private val clusterKeys = listOf("cluster_0", "cluster_1", "cluster_2")
    private var foodClusters: Map<String, List<String>> = emptyMap()

    override fun getItemCount(): Int = clusterTitles.size

    override fun createFragment(position: Int): Fragment {
        val foodList = foodClusters[clusterKeys[position]] ?: emptyList()
        Log.d("ViewPagerAdapter", "Position: $position, Food List: $foodList")
        return FoodFragment.newInstance(foodList)
    }

    fun updateClusters(clusters: Map<String, List<String>>) {
        foodClusters = clusters
        notifyDataSetChanged()
    }

    fun getPageTitle(position: Int): String = clusterTitles[position]
}
