package com.capstone.diabite.view.chatbot

import android.annotation.SuppressLint
import com.capstone.diabite.db.responses.ChatbotResponse
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.capstone.diabite.BuildConfig
import com.capstone.diabite.R
import com.capstone.diabite.databinding.ActivityChatbotBinding
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class ChatbotActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatbotBinding

    private var responseData = arrayListOf<ChatbotResponse>()

    private lateinit var adapter: ChatbotAdapter
    private var isResponding = false
    private var currentJob: Job? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.apply {

            backButton.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            adapter = ChatbotAdapter(this@ChatbotActivity, responseData)
            recyclerViewId.adapter = adapter

            askTextInputLayout.setEndIconOnClickListener {
                if (isResponding) {
                    stopResponse()
                } else {
                    val prompt = askEditText.text?.toString()
                    if (!prompt.isNullOrEmpty()) {
                        askEditText.setText("")
                        responseData.add(ChatbotResponse(0, prompt))
                        adapter.notifyDataSetChanged()
                        handlePrompt(prompt)
                        recyclerViewId.smoothScrollToPosition(responseData.size - 1)
                    }
                }
            }

            recyclerViewId.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val contentHeight = recyclerView.computeVerticalScrollRange()
                    val recyclerViewHeight = recyclerView.height

                    btnBottom.visibility = if (contentHeight > recyclerViewHeight) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            })

            btnBottom.setOnClickListener {
                val itemCount = recyclerViewId.adapter?.itemCount ?: 0
                if (itemCount > 0) {
                    recyclerViewId.smoothScrollToPosition(itemCount - 1)
                }
            }
        }
    }

    private fun handlePrompt(prompt: String) {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY
        )
//        responseData.add(ChatbotResponse(0, prompt))
//        adapter.notifyDataSetChanged()

        isResponding = true
        updateEndIcon()

        currentJob = lifecycleScope.launch {
            try {
                val response = generativeModel.generateContent(prompt)
                withContext(Dispatchers.Main) {
                    responseData.add(ChatbotResponse(1, response.text!!))
                    adapter.notifyDataSetChanged()
                }
            } catch (e: CancellationException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ChatbotActivity, "Response cancelled", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ChatbotActivity, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } finally {
                isResponding = false
                updateEndIcon()
            }
        }
    }

    private fun stopResponse() {
        currentJob?.cancel()
        currentJob = null
        isResponding = false
        updateEndIcon()
    }

    private fun addResponseToUI(responseText: String) {
        responseData.add(ChatbotResponse(1, responseText))
        adapter.notifyDataSetChanged()

        lifecycleScope.launch {
            delay(calculateAnimationDuration(responseText))
            isResponding = false
            updateEndIcon()
        }
    }

    private fun calculateAnimationDuration(responseText: String): Long {
        val charDelay = 500L
        return responseText.length * charDelay
    }
    private fun updateEndIcon() {
        val iconRes = if (isResponding) R.drawable.stop else R.drawable.send
        binding.askTextInputLayout.setEndIconDrawable(iconRes)
    }

    fun onResponseAnimationComplete() {
        isResponding = false
        updateEndIcon()
    }

}
