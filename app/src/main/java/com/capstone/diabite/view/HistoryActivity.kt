package com.capstone.diabite.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.diabite.databinding.ActivityHistoryBinding
import com.capstone.diabite.db.local.HistoryAdapter
import com.capstone.diabite.db.local.HistoryViewModel
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import com.capstone.diabite.R

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyVM: HistoryViewModel
    private lateinit var historyAdapter: HistoryAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        historyAdapter = HistoryAdapter(this, listOf())
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = historyAdapter

        historyVM = ViewModelProvider(this)[HistoryViewModel::class.java]

        historyVM.allHistory.observe(this) { historyList ->
            if (historyList.isEmpty()) {
                Toast.makeText(this, "No history data available", Toast.LENGTH_SHORT).show()
            } else {
                binding.rvHistory.visibility = View.VISIBLE
                historyAdapter.historyList = historyList
                historyAdapter.notifyDataSetChanged()
            }
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            private val backgroundColor = Color.parseColor("#f44336")
            private val background = ColorDrawable()
            private val deleteIcon = ContextCompat.getDrawable(this@HistoryActivity, R.drawable.trash_can_list)!!
            private val intrinsicWidth = deleteIcon.intrinsicWidth
            private val intrinsicHeight = deleteIcon.intrinsicHeight
            private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                val itemView = viewHolder.itemView
                val iconHeight = itemView.bottom - itemView.top
                val iconMargin = (iconHeight - intrinsicHeight) / 2
                val isCanceled = dX == 0f && !isCurrentlyActive

                if (isCanceled) {
                    clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, false)
                    return
                }


                if (dX < 0) {
                    background.color = backgroundColor
                    background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    deleteIcon.setBounds(
                        itemView.right - iconMargin - intrinsicWidth,
                        itemView.top + (iconHeight - intrinsicHeight) / 2,
                        itemView.right - iconMargin,
                        (itemView.top + (iconHeight - intrinsicHeight) / 2) + intrinsicHeight
                    )
                } else {
                    background.setBounds(0, 0, 0, 0)
                    deleteIcon.setBounds(0, 0, 0, 0)
                }

                background.draw(c)
                deleteIcon.draw(c)

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            }

            fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
                c?.drawRect(left, top, right, bottom, clearPaint)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val historyItem = historyAdapter.historyList[position]

                historyVM.deleteHistory(historyItem)
                historyAdapter.historyList = historyAdapter.historyList.toMutableList().apply {
                    removeAt(position)
                }
                historyAdapter.notifyItemRemoved(position)

                Toast.makeText(this@HistoryActivity, "History deleted", Toast.LENGTH_SHORT).show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvHistory)

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
